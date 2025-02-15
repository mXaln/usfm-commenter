package org.mxaln.compose.ui.control

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.import_from_api
import usfmcommenter.composeapp.generated.resources.import_usfm_file

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ImportFloatingButton(onFabMenuItemSelected: (MenuItem) -> Unit) {

    val items = mapOf(
        Icons.Default.Description to MenuItem.IMPORT_FILE,
        Icons.Default.CloudDownload to MenuItem.IMPORT_CLOUD
    )

    var fabMenuExpanded by remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = fabMenuExpanded,
                onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
                containerColor = ToggleFloatingActionButtonDefaults.containerColor(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.primaryVariant
                )
            ) {
                val imageVector by remember {
                    derivedStateOf {
                        if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                    }
                }
                Icon(
                    painter = rememberVectorPainter(imageVector),
                    contentDescription = null,
                    modifier = Modifier.animateIcon({ checkedProgress })
                )
            }
        }
    ) {
        items.forEach { (icon, item) ->
            FloatingActionButtonMenuItem(
                icon = { Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary) },
                onClick = {
                    fabMenuExpanded = false
                    onFabMenuItemSelected(item)
                },
                text = { Text(
                    text = stringResource(item.label),
                    color = MaterialTheme.colors.onPrimary) },
                containerColor = MaterialTheme.colors.primary
            )
        }
    }
}

enum class MenuItem(val label: StringResource) {
    IMPORT_FILE(Res.string.import_usfm_file),
    IMPORT_CLOUD(Res.string.import_from_api)
}