package org.mxaln.compose.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun getLocalizedString(input: Any): String {
    return when (input) {
        is String -> input
        is StringResource -> stringResource(input)
        else -> input.toString()
    }
}