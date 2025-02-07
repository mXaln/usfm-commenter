package org.mxaln.compose.dependencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val commentsDataSource: CommentDataSource
) : ViewModel() {

    val comments = commentsDataSource.getAllComments()

    fun addComment(
        verse: String,
        chapter: String,
        book: String,
        comment: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                commentsDataSource.createComment(
                    verse.toLong(),
                    chapter.toLong(),
                    book,
                    comment
                )
            }
        }
    }
}