package org.mxaln.compose.previews.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.api.ApiBook
import org.mxaln.compose.ui.dialog.ImportApiDialog
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun ImportApiDialogPreview() {
    val books = listOf(
        ApiBook(
            name = "01-GEN.usfm",
            downloadUrl = "https://example.com/01-GEN.usfm",
            size = 1024
        ),
        ApiBook(
            name = "02-EXO.usfm",
            downloadUrl = "https://example.com/02-EXO.usfm",
            size = 5124122
        ),
        ApiBook(
            name = "67-REV.usfm",
            downloadUrl = "https://example.com/67-REV.usfm",
            size = 623242
        )
    )

    MainAppTheme(themeColors = LightColors) {
        ImportApiDialog(
            books = books,
            onItemClicked = {},
            onDismiss = {}
        )
    }
}