package org.mxaln.compose.api

interface ApiError

sealed interface ApiResult<out D, out E: ApiError> {
    data class Success<out D>(val data: D) : ApiResult<D, Nothing>
    data class Error<out E: ApiError>(val error: E) : ApiResult<Nothing, E>
}

inline fun <T, E: ApiError, R> ApiResult<T, E>.map(map: (T) -> R): ApiResult<R, E> {
    return when(this) {
        is ApiResult.Error -> ApiResult.Error(error)
        is ApiResult.Success -> ApiResult.Success(map(data))
    }
}

fun <T, E: ApiError> ApiResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

inline fun <T, E: ApiError> ApiResult<T, E>.onSuccess(action: (T) -> Unit): ApiResult<T, E> {
    return when(this) {
        is ApiResult.Error -> this
        is ApiResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: ApiError> ApiResult<T, E>.onError(action: (E) -> Unit): ApiResult<T, E> {
    return when(this) {
        is ApiResult.Error -> {
            action(error)
            this
        }
        is ApiResult.Success -> this
    }
}

typealias EmptyResult<E> = ApiResult<Unit, E>