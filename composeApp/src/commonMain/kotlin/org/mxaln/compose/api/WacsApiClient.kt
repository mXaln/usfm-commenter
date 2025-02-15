package org.mxaln.compose.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsBytes
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.unknown_error

class WacsApiClient(
    private val httpClient: HttpClient
) {
    private companion object {
        const val BASE_URL = "https://content.bibletranslationtools.org/api/v1/repos"
        const val REPO_USER = "WycliffeAssociates"
        const val REPO_NAME = "en_ulb"
        const val CONTENTS = "contents"
        const val REF = "master"
    }

    suspend fun fetchBooks(ref: String = REF): ApiResult<List<ApiBook>, NetworkError> {
        val response = getResponse(buildUrl(), mapOf("ref" to ref))

        return when {
            response.data != null -> {
                ApiResult.Success(
                    response.data.body<List<ApiBook>>()
                        .filter { it.downloadUrl != null }
                        .filter { it.name.endsWith(".usfm", ignoreCase = true) }
                )
            }
            response.error != null -> {
                ApiResult.Error(response.error)
            }
            else -> ApiResult.Error(
                NetworkError(ErrorType.Unknown, Res.string.unknown_error)
            )
        }
    }

    suspend fun downloadBook(url: String): ApiResult<ByteArray, NetworkError> {
        val response = getResponse(url)

        return when {
            response.data != null -> {
                ApiResult.Success(response.data.bodyAsBytes())
            }
            response.error != null -> {
                ApiResult.Error(response.error)
            }
            else -> ApiResult.Error(
                NetworkError(ErrorType.Unknown, "Unknown error occurred")
            )
        }
    }

    private suspend fun getResponse(
        url: String,
        params: Map<String, String> = mapOf()
    ): NetworkResponse {
        val response = try {
            httpClient.get(url) {
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        } catch(e: UnresolvedAddressException) {
            return NetworkResponse(null, NetworkError(ErrorType.NoInternet, e.message))
        } catch(e: SerializationException) {
            return NetworkResponse(null, NetworkError(ErrorType.Serialization, e.message))
        } catch (e: HttpRequestTimeoutException) {
            return NetworkResponse(null, NetworkError(ErrorType.RequestTimeout, e.message))
        } catch (e: Exception) {
            return NetworkResponse(
                null,
                NetworkError(ErrorType.Unknown, Res.string.unknown_error)
            )
        }

        return when (response.status.value) {
            in 200..299 -> NetworkResponse(response, null)
            413 -> NetworkResponse(
                null,
                NetworkError(ErrorType.PayloadTooLarge, response.status.description)
            )
            in 500..599 -> NetworkResponse(
                null,
                NetworkError(ErrorType.ServerError, response.status.description)
            )
            else -> NetworkResponse(
                null,
                NetworkError(ErrorType.Unknown, Res.string.unknown_error)
            )
        }
    }

    private fun buildUrl(repoUser: String = REPO_USER, repoName: String = REPO_NAME): String {
        val url = StringBuilder(BASE_URL)
        url.append("/")
        url.append(repoUser)
        url.append("/")
        url.append(repoName)
        url.append("/")
        url.append(CONTENTS)
        return url.toString()
    }
}