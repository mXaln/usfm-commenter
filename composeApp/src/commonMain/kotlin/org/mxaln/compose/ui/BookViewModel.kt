package org.mxaln.compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.compose.domain.CommentDataSource
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.database.Book
import org.mxaln.database.Comment

class BookViewModel(
    private val commentsDataSource: CommentDataSource,
    private val usfmBookSource: UsfmBookSource
) : ViewModel() {

    private var book by mutableStateOf<Book?>(null)

    val chapters = MutableStateFlow<List<Chapter>>(emptyList())
    val comments = MutableSharedFlow<List<Comment>>()

    suspend fun loadBook(book: Book) {
        this.book = book
        parseBook(book)
        comments.emitAll(commentsDataSource.getByBook(book.id))
    }

    fun addComment(
        chapter: Chapter,
        verse: Verse,
        comment: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                commentsDataSource.add(
                    verse.number.toLong(),
                    chapter.number.toLong(),
                    comment,
                    book!!.id
                )
            }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            commentsDataSource.delete(comment.id)
        }
    }

    private fun parseBook(book: Book) {
        viewModelScope.launch {
            chapters.emit(usfmBookSource.parse(book))
        }
    }
}