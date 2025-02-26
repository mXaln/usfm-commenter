package org.mxaln.compose.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val slug: String,
    val name: String,
    val content: String,
    @SerialName("_id")
    val id: Long? = null,
)
