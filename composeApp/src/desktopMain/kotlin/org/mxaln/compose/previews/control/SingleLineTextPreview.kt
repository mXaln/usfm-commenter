package org.mxaln.compose.previews.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import org.mxaln.compose.ui.control.SingleLineText

@Preview
@Composable
fun SingleLineTextPreview() {
    val longText = """
        This is a line 1
        This is a line 2
    """.trimIndent()

    SingleLineText(text = longText)
}