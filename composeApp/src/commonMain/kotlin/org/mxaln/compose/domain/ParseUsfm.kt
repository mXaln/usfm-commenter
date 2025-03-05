package org.mxaln.compose.domain

import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.compose.usfm.AppUsfmParser
import org.mxaln.compose.usfm.CMarker
import org.mxaln.compose.usfm.FMarker
import org.mxaln.compose.usfm.TextBlock
import org.mxaln.compose.usfm.UsfmDocument
import org.mxaln.compose.usfm.VMarker
import org.mxaln.compose.usfm.XMarker

object ParseUsfm {

    suspend fun getDocument(usfm: String): UsfmDocument {
        val usfmParser = AppUsfmParser(arrayListOf("s5"), true)
        return usfmParser.parseFromString(usfm)
    }

    suspend fun getChapters(usfm: String): List<Chapter> {
        val document = getDocument(usfm)
        return getChapters(document)
    }

    private fun getChapters(document: UsfmDocument): List<Chapter> {
        return document.getChildMarkers(CMarker::class).map { chapter ->
            Chapter(
                number = chapter.number,
                verses = getVerses(chapter)
            )
        }
    }

    private fun getVerses(chapter: CMarker): List<Verse> {
        return chapter.getChildMarkers(VMarker::class).map { verse ->
            Verse(
                number = verse.startingVerse,
                text = verse.getText()
            )
        }.filter { it.number > 0 }
    }
}

fun VMarker.getText(): String {
    val ignoredMarkers = listOf(FMarker::class, XMarker::class)
    val textBlocks = getChildMarkers(TextBlock::class, ignoredMarkers)
    val sb = StringBuilder()
    for ((idx, textBlock) in textBlocks.withIndex()) {
        sb.append(textBlock.text.trim())
        if (idx != textBlocks.lastIndex) {
            sb.append(" ")
        }
    }
    return sb.toString()
}