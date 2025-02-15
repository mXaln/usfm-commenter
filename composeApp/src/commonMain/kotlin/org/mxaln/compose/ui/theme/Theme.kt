package org.mxaln.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = Color(0xFFd85900),
    primaryVariant = Color(0xFFB44A00),
    secondary = Color(0xFF8CBAD1),
    background = Color(0xFFF3F3F3),
    surface = Color(0xFFF3F3F3),
    onPrimary = Color(0xFFF3F3F3),
    onSecondary = Color.Black,
    onSurface = Color.Black
)

private val DarkColors = darkColors(
    primary = Color(0xFFE08500),
    primaryVariant = Color(0xFFBC7000),
    secondary = Color(0xFF607D8B),
    background = Color(0xFF19191A),
    surface = Color(0xFF19191A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFC9C9C9),
    onSurface = Color(0xFFC9C9C9)
)

@Composable
fun MainAppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(
        colors = colors,
        content = content
    )
}
