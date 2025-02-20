package org.mxaln.compose.ui.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Composable
fun EmptyBookCard() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
            .requiredHeight(72.dp)
    ) {
        Text("")
    }
}

@Composable
@Preview
fun EmptyBookCardPreview() {
    MainAppTheme(themeColors = LightColors) {
        EmptyBookCard()
    }
}