package org.mxaln.compose.api

data class NetworkError(
    val type: ErrorType,
    val description: Any?
) : ApiError

enum class ErrorType {
    NoInternet,
    Serialization,
    RequestTimeout,
    PayloadTooLarge,
    ServerError,
    Unknown;
}