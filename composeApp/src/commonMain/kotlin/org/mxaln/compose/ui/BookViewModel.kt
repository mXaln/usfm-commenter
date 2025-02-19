package org.mxaln.compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.compose.domain.CommentDataSource
import org.mxaln.compose.domain.UsfmBookSource
import org.mxaln.database.Book
import org.mxaln.database.Comment
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.parsing_book_wait

class BookViewModel(
    private val commentsDataSource: CommentDataSource,
    private val usfmBookSource: UsfmBookSource,
    private val book: Book
) : ScreenModel {

    private val _chapters = MutableStateFlow(listOf<Chapter>())
    val chapters = _chapters.onStart { parseBook() }

    private val _comments = MutableStateFlow(listOf<Comment>())
    val comments = _comments.asStateFlow()

    var progress by mutableStateOf<Any?>(null)
        private set

    fun addComment(
        chapter: Chapter,
        verse: Verse,
        comment: String
    ) {
        screenModelScope.launch {
            withContext(Dispatchers.IO) {
                commentsDataSource.add(
                    verse.number.toLong(),
                    chapter.number.toLong(),
                    comment,
                    book.id
                )
            }
        }
    }

    fun deleteComment(comment: Comment) {
        screenModelScope.launch {
            commentsDataSource.delete(comment.id)
        }
    }

    private fun parseBook() {
        screenModelScope.launch {
            progress = Res.string.parsing_book_wait
            _chapters.value = usfmBookSource.parse(book)
            loadComments()
            progress = null

        }
    }

    private fun loadComments() {
        screenModelScope.launch {
            _comments.emitAll(commentsDataSource.getByBook(book.id))
        }
    }
}