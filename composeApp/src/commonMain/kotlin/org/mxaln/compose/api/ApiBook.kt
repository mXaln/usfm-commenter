package org.mxaln.compose.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiBook(
    val name: String,
    val size: Int,
    @SerialName("download_url")
    val downloadUrl: String? = null
)