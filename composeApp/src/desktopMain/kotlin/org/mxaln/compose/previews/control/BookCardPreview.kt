package org.mxaln.compose.previews.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.ui.control.BookCard
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme
import org.mxaln.database.Book

@Composable
@Preview
fun BookCardPreview() {
    MainAppTheme(themeColors = LightColors) {
        BookCard(
            book = Book(0, "test", "test", "test"),
            onSelect = {},
            onDelete = {}
        )
    }
}