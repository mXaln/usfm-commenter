package org.mxaln.compose.domain

import org.mxaln.compose.usfm.HMarker
import org.mxaln.compose.usfm.TOC3Marker

class ImportUsfm(private val bookDataSource: BookDataSource) {

    suspend fun import(bytes: ByteArray) {
        try {
            val usfm = bytes.decodeToString()
            val document = ParseUsfm.getDocument(usfm)

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
}