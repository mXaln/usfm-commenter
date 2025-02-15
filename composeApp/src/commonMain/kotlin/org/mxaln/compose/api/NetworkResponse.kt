package org.mxaln.compose.api

import io.ktor.client.statement.HttpResponse

data class NetworkResponse(
    val data: HttpResponse?,
    val error: NetworkError?
)
