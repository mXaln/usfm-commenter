package org.mxaln.compose.previews.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.data.Comment
import org.mxaln.compose.ui.dialog.CommentDialog
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun CommentDialogPreview() {
    MainAppTheme(themeColors = LightColors) {
        val comments = listOf(
            Comment(
                id = 1,
                verse = 1,
                chapter = 1,
                comment = "This is the first comment",
                bookId = 1
            ),
            Comment(
                id = 2,
                verse = 1,
                chapter = 1,
                comment = "This is the second comment",
                bookId = 1
            )
        )

        CommentDialog(
            title = "Genesis 1:1",
            comments = comments,
            onSave = {},
            onDelete = {},
            onDismiss = {}
        )
    }
}