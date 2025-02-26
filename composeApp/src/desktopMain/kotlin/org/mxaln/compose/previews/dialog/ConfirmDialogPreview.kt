package org.mxaln.compose.previews.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.ui.dialog.ConfirmDialog
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun ConfirmDialogPreview() {
    MainAppTheme(themeColors = LightColors) {
        ConfirmDialog(
            message = "Are you sure you want to delete this item?",
            onConfirm = {},
            onCancel = {},
            onDismiss = {}
        )
    }
}