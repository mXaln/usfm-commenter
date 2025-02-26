package org.mxaln.compose.previews.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.ui.dialog.ErrorDialog
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun ErrorDialogPreview() {
    MainAppTheme(themeColors = LightColors) {
        ErrorDialog(
            error = "This is a test error message.",
            onDismiss = {}
        )
    }
}