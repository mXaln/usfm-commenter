package org.mxaln.compose

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mxaln.compose.ui.HomeScreen
import org.mxaln.compose.ui.theme.MainAppTheme

@Composable
@Preview
fun App() {
    MainAppTheme {
        Navigator(HomeScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}