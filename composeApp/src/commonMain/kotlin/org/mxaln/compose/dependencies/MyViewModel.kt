package org.mxaln.compose.dependencies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zwander.kotlin.file.IPlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.readString
import org.wycliffeassociates.usfmtools.USFMParser
import org.wycliffeassociates.usfmtools.models.markers.CMarker
import org.wycliffeassociates.usfmtools.models.markers.FMarker
import org.wycliffeassociates.usfmtools.models.markers.HMarker
import org.wycliffeassociates.usfmtools.models.markers.TextBlock
import org.wycliffeassociates.usfmtools.models.markers.VMarker
import org.wycliffeassociates.usfmtools.models.markers.XMarker

class MyViewModel(
    private val commentsDataSource: CommentDataSource,
    private val httpClient: HttpClient,
    private val directoryProvider: DirectoryProvider
) : ViewModel() {

    val comments = commentsDataSource.getAllComments()

    val usfmOutput = mutableStateOf("")

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

    fun downloadUsfm(url: String) {
        viewModelScope.launch {
            try {
                val response = httpClient.get {
                    url(url)
                }

                val usfm = response.bodyAsText()
                usfmOutput.value = try {
                    parseUsfm(usfm)
                } catch (e: Exception) {
                    e.message ?: "Error"
                }

                var fileName = Url(url).segments.last()
                if (!fileName.contains(".")) {
                    fileName = "default.usfm"
                }
                directoryProvider.saveDocument(response.bodyAsBytes(), fileName)
            } catch (e: ResponseException) {
                usfmOutput.value = "Error: ${e.response.status.description}"
            } catch (e: Exception) {
                usfmOutput.value = "Error: ${e.message}"
            }
        }
    }

    fun importUsfm(file: IPlatformFile) {
        viewModelScope.launch {
            file.openInputStream().use { stream ->
                stream?.let {
                    usfmOutput.value = try {
                        parseUsfm(it.readString())
                    } catch (e: Exception) {
                        e.message ?: "Error"
                    }
                }
            }
            directoryProvider.saveDocument(file)
        }
    }

    fun readUsfm(fileName: String) {
        viewModelScope.launch {
            usfmOutput.value = directoryProvider.readDocument(fileName)?.let { text ->
                parseUsfm(text)
            } ?: "Could not read USFM file"
        }
    }

    private fun parseUsfm(usfm: String): String {
        val parser = USFMParser(arrayListOf("s5"), true)
        val document = parser.parseFromString(usfm)
        val book = document.getChildMarkers(HMarker::class.java).firstOrNull()
        val chapters = document.getChildMarkers(CMarker::class.java)

        val text = StringBuilder()
        chapters
            .forEach { chapter ->
                text.append(book?.headerText)
                text.append("\n")
                text.append("Chapter ${chapter.number}")
                text.append("\n")

                val verses = chapter.getChildMarkers(VMarker::class.java)
                verses.forEach { verse ->
                    text.append("${verse.verseNumber}. ")
                    text.append(verse.getText())
                    text.append("\n")
                }

                text.append("\n")
            }

        return text.toString()
    }
}

fun VMarker.getText(): String {
    val ignoredMarkers = listOf<Class<*>>(FMarker::class.java, XMarker::class.java)
    val text = this.getChildMarkers(TextBlock::class.java, ignoredMarkers)
    val sb = StringBuilder()
    for ((idx, txt) in text.withIndex()) {
        sb.append(txt.text.trim())
        if (idx != text.lastIndex) {
            sb.append(" ")
        }
    }
    return sb.toString()
}