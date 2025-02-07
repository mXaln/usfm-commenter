package org.mxaln.compose

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.mxaln.compose.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MaxProject",
        ) {
            App()
        }
    }
}