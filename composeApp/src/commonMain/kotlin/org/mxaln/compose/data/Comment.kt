package org.mxaln.compose.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val verse: Long,
    val chapter: Long,
    val comment: String,
    val bookId: Long,
    @SerialName("_id")
    val id: Long? = null
)
