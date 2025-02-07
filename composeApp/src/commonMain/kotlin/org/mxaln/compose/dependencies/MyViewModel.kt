package org.mxaln.compose.dependencies

import androidx.lifecycle.ViewModel

class MyViewModel(
    private val commentsDataSource: CommentDataSource
) : ViewModel() {

    val comments = commentsDataSource.getAllComments()


}