package org.mxaln.compose.dependencies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wycliffeassociates.usfmtools.USFMParser
import org.wycliffeassociates.usfmtools.models.markers.CMarker
import org.wycliffeassociates.usfmtools.models.markers.FMarker
import org.wycliffeassociates.usfmtools.models.markers.TextBlock
import org.wycliffeassociates.usfmtools.models.markers.VMarker
import org.wycliffeassociates.usfmtools.models.markers.XMarker

class MyViewModel(
    private val commentsDataSource: CommentDataSource
) : ViewModel() {

    val comments = commentsDataSource.getAllComments()

    val test = mutableStateOf(getTestUsfm())

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

    private fun getTestUsfm(): String {
        val usfm = """
            \c 1
            \v 1 Hello World
            \v 2 This is test
            \c 2
            \v 1 This is a verse 1
            \v 2 This is verse 2
        """.trimIndent()

        val parser = USFMParser(arrayListOf<String>(), true)
        val document = parser.parseFromString(usfm)
        val chapters = document.getChildMarkers(CMarker::class.java)

        val text = StringBuilder()
        chapters
            .forEach { chapter ->
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