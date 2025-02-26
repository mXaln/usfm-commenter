package org.mxaln.compose.previews.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Comment
import org.mxaln.compose.data.Verse
import org.mxaln.compose.ui.control.ChapterCard
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun ChapterCardPreview() {
    val chapter = Chapter(
        number = 1,
        verses = listOf(
            Verse(1, "This is a verse number 1"),
            Verse(2, "This is a verse number 2"),
            Verse(3, "This is a verse number 3")
        )
    )
    val comments = listOf(
        Comment(
        id = 1,
        verse = 2,
        chapter = 1,
        comment = "test comment",
        bookId = 1
    )
    )

    MainAppTheme(themeColors = LightColors) {
        ChapterCard(
            chapter = chapter,
            comments = comments,
            onVerseClick = {}
        )
    }
}