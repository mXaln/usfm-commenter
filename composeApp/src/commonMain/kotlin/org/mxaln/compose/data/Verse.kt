package org.mxaln.compose.data

import org.mxaln.database.Comment

data class Verse(
    val number: Int,
    val text: String,
    val comment: Comment?
)
