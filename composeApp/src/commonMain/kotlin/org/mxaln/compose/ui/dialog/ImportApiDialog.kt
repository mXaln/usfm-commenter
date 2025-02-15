package org.mxaln.compose.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.mxaln.compose.api.ApiBook
import org.mxaln.compose.utils.Utils
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.dismiss
import usfmcommenter.composeapp.generated.resources.import_from_api

@Composable
fun ImportApiDialog(
    books: List<ApiBook>,
    onItemClicked: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = 5.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 16.dp)
            ) {
                Text(
                    stringResource(Res.string.import_from_api),
                    style = MaterialTheme.typography.h5,
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(min = 0.dp, max = 400.dp)
                ) {
                    items(books) { book ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    onItemClicked(book.downloadUrl!!)
                                    onDismiss()
                                }
                        ) {
                            Text(book.name, modifier = Modifier.weight(1f))
                            Text(Utils.bytesToHumanReadableSize(book.size.toDouble()))
                        }
                    }
                }

                Button(onClick = onDismiss) {
                    Text(stringResource(Res.string.dismiss))
                }
            }
        }
    }
}