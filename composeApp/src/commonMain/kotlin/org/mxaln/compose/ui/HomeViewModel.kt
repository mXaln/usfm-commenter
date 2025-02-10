package org.mxaln.compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zwander.kotlin.file.IPlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.domain.BookDataSource
import org.mxaln.compose.domain.DirectoryProvider
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.database.Book

class HomeViewModel(
    private val httpClient: HttpClient,
    private val directoryProvider: DirectoryProvider,
    private val bookDataSource: BookDataSource,
    private val usfmBookSource: UsfmBookSource
) : ViewModel() {

    val books = bookDataSource.getAll()
    var error by mutableStateOf("")

    fun downloadUsfm(url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = httpClient.get {
                        url(url)
                    }
                    usfmBookSource.import(response.bodyAsBytes())
                } catch (e: ResponseException) {
                    error = "Error: ${e.response.status.description}"
                } catch (e: Exception) {
                    error = "Error: ${e.message}"
                }
            }
        }
    }

    fun importUsfm(file: IPlatformFile) {
        viewModelScope.launch {
            try {
                usfmBookSource.import(file)
            } catch (e: Exception) {
                var message: String
                if (e.message != null) {
                    message = e.message!!
                    if (e.cause?.message != null) {
                        message += " ${e.cause?.message!!}"
                    }
                } else {
                    message = "Unknown error occurred."
                }
                error = message
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                directoryProvider.deleteDocument(book.document)
                bookDataSource.delete(book.id)
            }
        }
    }
}