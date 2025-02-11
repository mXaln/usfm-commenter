package org.mxaln.compose

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.stringResource
import org.mxaln.compose.di.initKoin
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.app_name

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
        ) {
            App()
        }
    }
}