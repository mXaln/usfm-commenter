package org.mxaln.compose.previews.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.mxaln.compose.ui.control.ImportFloatingMenu
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme

@Preview
@Composable
fun ImportFloatingMenuPreview() {
    MainAppTheme(themeColors = LightColors) {
        Scaffold(
            floatingActionButton = {
                ImportFloatingMenu(
                    expandedState = remember { mutableStateOf(false) },
                    onFabMenuItemSelected = {}
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Content", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}