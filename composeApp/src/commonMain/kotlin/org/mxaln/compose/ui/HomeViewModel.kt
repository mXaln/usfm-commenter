package org.mxaln.compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.api.ApiBook
import org.mxaln.compose.api.WacsApiClient
import org.mxaln.compose.api.onError
import org.mxaln.compose.api.onSuccess
import org.mxaln.compose.domain.BookDataSource
import org.mxaln.compose.domain.ImportUsfm
import org.mxaln.compose.ui.dialog.ConfirmAction
import org.mxaln.database.Book
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.delete_book_confirmation
import usfmcommenter.composeapp.generated.resources.downloading_book_wait
import usfmcommenter.composeapp.generated.resources.importing_book_wait
import usfmcommenter.composeapp.generated.resources.loading_books_wait
import usfmcommenter.composeapp.generated.resources.unknown_error

class HomeViewModel(
    private val bookDataSource: BookDataSource,
    private val importUsfm: ImportUsfm,
    private val wacsApiClient: WacsApiClient
) : ScreenModel {

    var books = MutableStateFlow(listOf<Book>())
        private set

    private val _apiBooks = MutableStateFlow(listOf<ApiBook>())
    val apiBooks = _apiBooks.asStateFlow()

    var error by mutableStateOf<Any?>(null)
        private set
    var progress by mutableStateOf<Any?>(null)
        private set
    var showBookDialog by mutableStateOf(false)
        private set
    var confirmAction by mutableStateOf<ConfirmAction?>(null)
        private set

    init {
        loadBooks()
    }

    fun downloadUsfm(url: String) {
        screenModelScope.launch {
            progress = Res.string.downloading_book_wait
            withContext(Dispatchers.Default) {
                val response = wacsApiClient.downloadBook(url)
                response.onSuccess { bytes ->
                    importUsfm.import(bytes)
                    loadBooks()
                }.onError { err ->
                    error = err.description ?: Res.string.unknown_error
                }
            }
            progress = null
        }
    }

    fun importUsfm(file: PlatformFile) {
        screenModelScope.launch {
            progress = Res.string.importing_book_wait
            try {
                importUsfm.import(file.readBytes())
                loadBooks()
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
                error = message
            }
            progress = null
        }
    }

    fun deleteBook(book: Book) {
        confirmAction = ConfirmAction(
            message = Res.string.delete_book_confirmation,
            onConfirm = {
                screenModelScope.launch {
                    withContext(Dispatchers.Default) {
                        bookDataSource.delete(book.id)
                    }
                    loadBooks()
                }
            },
            onCancel = { confirmAction = null }
        )
    }

    fun showImportBookDialog() {
        if (apiBooks.value.isEmpty()) {
            loadApiBooks()
        } else {
            showBookDialog = true
        }
    }

    fun clearError() {
        error = null
    }

    fun hideBookDialog() {
        showBookDialog = false
    }

    fun clearConfirmAction() {
        confirmAction = null
    }

    private fun loadBooks() {
        screenModelScope.launch {
            books.emitAll(bookDataSource.getAll())
        }
    }

    private fun loadApiBooks() {
        screenModelScope.launch {
            progress = Res.string.loading_books_wait
            wacsApiClient.fetchBooks()
                .onSuccess { books ->
                    _apiBooks.emit(books)
                    showBookDialog = true
                }
                .onError { err ->
                    error = err.description ?: Res.string.unknown_error
                }
            progress = null
        }
    }
}