package org.mxaln.compose.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.api.ApiBook
import org.mxaln.compose.api.WacsApiClient
import org.mxaln.compose.api.onError
import org.mxaln.compose.api.onSuccess
import org.mxaln.compose.domain.BookDataSource
import org.mxaln.compose.domain.DirectoryProvider
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.compose.ui.dialog.ConfirmAction
import org.mxaln.database.Book
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.delete_book_confirmation
import usfmcommenter.composeapp.generated.resources.downloading_book_wait
import usfmcommenter.composeapp.generated.resources.importing_book_wait
import usfmcommenter.composeapp.generated.resources.loading_books_wait
import usfmcommenter.composeapp.generated.resources.unknown_error

class HomeViewModel(
    private val directoryProvider: DirectoryProvider,
    private val bookDataSource: BookDataSource,
    private val usfmBookSource: UsfmBookSource,
    private val wacsApiClient: WacsApiClient
) : ViewModel() {

    val books = bookDataSource.getAll()
    val apiBooks = MutableStateFlow(listOf<ApiBook>())

    val error = mutableStateOf<Any?>(null)
    val progress = mutableStateOf<Any?>(null)
    val showBookDialog = mutableStateOf(false)
    val confirmAction = mutableStateOf<ConfirmAction?>(null)

    fun downloadUsfm(url: String) {
        viewModelScope.launch {
            progress.value = Res.string.downloading_book_wait
            withContext(Dispatchers.IO) {
                val response = wacsApiClient.downloadBook(url)
                response.onSuccess { bytes ->
                    usfmBookSource.import(bytes)
                }.onError { err ->
                    error.value = err.description ?: Res.string.unknown_error
                }
            }
            progress.value = null
        }
    }

    fun importUsfm(file: IPlatformFile) {
        viewModelScope.launch {
            progress.value = Res.string.importing_book_wait
            try {
                usfmBookSource.import(file)
            } catch (e: Exception) {
                var message: Any
                if (e.message != null) {
                    message = e.message!!
                    if (e.cause?.message != null) {
                        message += " ${e.cause?.message!!}"
                    }
                } else {
                    message = Res.string.unknown_error
                }
                error.value = message
            }
            progress.value = null
        }
    }

    fun deleteBook(book: Book) {
        confirmAction.value = ConfirmAction(
            message = Res.string.delete_book_confirmation,
            onConfirm = {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        directoryProvider.deleteDocument(book.document)
                        bookDataSource.delete(book.id)
                    }
                }
            },
            onCancel = { confirmAction.value = null }
        )
    }

    fun showImportBookDialog() {
        if (apiBooks.value.isEmpty()) {
            loadApiBooks()
        } else {
            showBookDialog.value = true
        }
    }

    private fun loadApiBooks() {
        viewModelScope.launch {
            progress.value = Res.string.loading_books_wait
            wacsApiClient.fetchBooks()
                .onSuccess { books ->
                    apiBooks.emit(books)
                    showBookDialog.value = true
                }
                .onError { err ->
                    error.value = err.description ?: Res.string.unknown_error
                }
            progress.value = null
        }
    }
}