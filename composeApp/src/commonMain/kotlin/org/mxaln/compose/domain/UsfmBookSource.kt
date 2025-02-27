package org.mxaln.compose.domain

import org.mxaln.compose.data.Book
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.compose.usfm.AppUsfmParser
import org.mxaln.compose.usfm.CMarker
import org.mxaln.compose.usfm.FMarker
import org.mxaln.compose.usfm.HMarker
import org.mxaln.compose.usfm.TOC3Marker
import org.mxaln.compose.usfm.TextBlock
import org.mxaln.compose.usfm.UsfmDocument
import org.mxaln.compose.usfm.VMarker
import org.mxaln.compose.usfm.XMarker

interface UsfmBookSource {
    suspend fun import(bytes: ByteArray)
    suspend fun parse(book: Book): List<Chapter>
}

class UsfmBookSourceImpl(
    private val bookDataSource: BookDataSource
) : UsfmBookSource {

    override suspend fun import(bytes: ByteArray) {
        try {
            val usfm = bytes.decodeToString()
            val usfmParser = AppUsfmParser(arrayListOf("s5"), true)
            val document = usfmParser.parseFromString(usfm)

            val bookSlug = document
                .getChildMarkers(TOC3Marker::class)
                .firstOrNull()
                ?.bookAbbreviation
                ?.lowercase()

            val bookName = document
                .getChildMarkers(HMarker::class)
                .firstOrNull()
                ?.headerText

            if (bookSlug == null || bookName == null) {
                throw IllegalArgumentException("Book header is not complete.")
            }

            val existentBook = bookDataSource.getBySlug(bookSlug)
            if (existentBook != null) {
                bookDataSource.update(existentBook.copy(content = usfm))
            } else {
                bookDataSource.add(
                    slug = bookSlug,
                    name = bookName,
                    content = usfm
                )
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not import file.", e)
        }
    }

    override suspend fun parse(book: Book): List<Chapter> {
        val usfmParser = AppUsfmParser(arrayListOf("s5"), true)
        val document = usfmParser.parseFromString(book.content)
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