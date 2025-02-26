package org.mxaln.compose.previews.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.ui.control.EmptyBookCard
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Composable
@Preview
fun EmptyBookCardPreview() {
    MainAppTheme(themeColors = LightColors) {
        EmptyBookCard()
    }
}