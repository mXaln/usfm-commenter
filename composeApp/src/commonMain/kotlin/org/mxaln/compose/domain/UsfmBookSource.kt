package org.mxaln.compose.domain

import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.io.asOutputStream
import kotlinx.io.readString
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.database.Book
import org.wycliffeassociates.usfmtools.USFMParser
import org.wycliffeassociates.usfmtools.models.markers.CMarker
import org.wycliffeassociates.usfmtools.models.markers.FMarker
import org.wycliffeassociates.usfmtools.models.markers.HMarker
import org.wycliffeassociates.usfmtools.models.markers.TOC3Marker
import org.wycliffeassociates.usfmtools.models.markers.TextBlock
import org.wycliffeassociates.usfmtools.models.markers.USFMDocument
import org.wycliffeassociates.usfmtools.models.markers.VMarker
import org.wycliffeassociates.usfmtools.models.markers.XMarker

interface UsfmBookSource {
    suspend fun import(file: IPlatformFile)
    suspend fun import(bytes: ByteArray)
    suspend fun parse(book: Book): List<Chapter>
}

class UsfmBookSourceImpl(
    private val directoryProvider: DirectoryProvider,
    private val bookDataSource: BookDataSource
) : UsfmBookSource {

    override suspend fun import(file: IPlatformFile) {
        try {
            file.openInputStream()?.use { stream ->
                val usfm = stream.readString()
                val usfmParser = USFMParser(arrayListOf("s5"), true)
                val document = usfmParser.parseFromString(usfm)

                val bookSlug = document
                    .getChildMarkers(TOC3Marker::class.java)
                    .firstOrNull()
                    ?.bookAbbreviation
                    ?.lowercase()

                val bookName = document
                    .getChildMarkers(HMarker::class.java)
                    .firstOrNull()
                    ?.headerText

                if (bookSlug == null || bookName == null) {
                    throw IllegalArgumentException("Book header is not complete.")
                }

                val newFile = directoryProvider.saveDocument(file)
                val existentBook = bookDataSource.getBySlug(bookSlug)
                if (existentBook != null) {
                    directoryProvider.deleteDocument(existentBook.document)
                    bookDataSource.update(existentBook.copy(document = newFile.getName()))
                } else {
                    bookDataSource.add(
                        slug = bookSlug,
                        name = bookName,
                        path = newFile.getName()
                    )
                }
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not import file.", e)
        }
    }

    override suspend fun import(bytes: ByteArray) {
        try {
            val tempFile = directoryProvider.createTempFile("temp", ".usfm")
            bytes.inputStream().use { input ->
                tempFile.openOutputStream()?.use { output ->
                    input.copyTo(output.asOutputStream(), 1024)
                }
            }
            import(tempFile)
            tempFile.delete()
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not import file.", e)
        }
    }

    override suspend fun parse(book: Book): List<Chapter> {
        return directoryProvider.readDocument(book.document)?.let { usfm ->
            val usfmParser = USFMParser(arrayListOf("s5"), true)
            val document = usfmParser.parseFromString(usfm)
            getChapters(document)
        } ?: throw IllegalArgumentException("Could not parse file.")
    }

    private suspend fun getChapters(document: USFMDocument, ): List<Chapter> {
        return document.getChildMarkers(CMarker::class.java).map { chapter ->
            Chapter(
                number = chapter.number,
                verses = getVerses(chapter)
            )
        }
    }

    private suspend fun getVerses(chapter: CMarker, ): List<Verse> {
        return chapter.getChildMarkers(VMarker::class.java).map { verse ->
            Verse(
                number = verse.startingVerse,
                text = verse.getText()
            )
        }
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