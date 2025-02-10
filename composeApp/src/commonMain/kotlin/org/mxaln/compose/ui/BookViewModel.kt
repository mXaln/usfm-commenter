package org.mxaln.compose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.domain.CommentDataSource
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.database.Book

class BookViewModel(
    private val commentsDataSource: CommentDataSource,
    private val usfmBookSource: UsfmBookSource
) : ViewModel() {

    val chapters = MutableStateFlow<List<Chapter>>(emptyList())

    fun addComment(
        verse: String,
        chapter: String,
        comment: String,
        bookId: Long
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                commentsDataSource.add(
                    verse.toLong(),
                    chapter.toLong(),
                    comment,
                    bookId
                )
            }
        }
    }

    fun deleteComment(id: Long) {
        viewModelScope.launch {
            commentsDataSource.delete(id)
        }
    }

    fun parseBook(book: Book) {
        viewModelScope.launch {
            chapters.emit(usfmBookSource.parse(book))
        }
    }
}