package org.mxaln.compose.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mxaln.compose.ui.control.SingleLineText
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.cancel
import usfmcommenter.composeapp.generated.resources.yes

data class ConfirmAction(
    val message: StringResource,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)

@Composable
fun ConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 5.dp
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("")
                Text(message)
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        modifier = Modifier.width(128.dp)
                    ) {
                        SingleLineText(stringResource(Res.string.yes))
                    }
                    Button(
                        onClick = {
                            onCancel()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        modifier = Modifier.width(128.dp)
                    ) {
                        SingleLineText(stringResource(Res.string.cancel))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    MainAppTheme(themeColors = LightColors) {
        ConfirmDialog(
            message = "Are you sure you want to delete this item?",
            onConfirm = {},
            onCancel = {},
            onDismiss = {}
        )
    }
}