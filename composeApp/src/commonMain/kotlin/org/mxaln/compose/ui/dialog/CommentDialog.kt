package org.mxaln.compose.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.mxaln.compose.ui.control.SingleLineText
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme
import org.mxaln.database.Comment
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.add_comment
import usfmcommenter.composeapp.generated.resources.dismiss
import usfmcommenter.composeapp.generated.resources.enter_comment

@Composable
fun CommentDialog(
    title: String,
    comments: List<Comment>,
    onSave: (String) -> Unit,
    onDelete: (Comment) -> Unit,
    onDismiss: () -> Unit
) {
    var commentInput by remember { mutableStateOf("") }

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
                    title,
                    style = MaterialTheme.typography.h5,
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(min = 0.dp, max = 200.dp)
                ) {
                    items(comments) { comment ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(comment.comment, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onDelete(comment)
                                }
                            )
                        }
                    }
                }

                TextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    placeholder = { Text(stringResource(Res.string.enter_comment)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSave(commentInput)
                            commentInput = ""
                        },
                        modifier = Modifier.width(144.dp)
                    ) {
                        SingleLineText(stringResource(Res.string.add_comment))
                    }
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        modifier = Modifier.width(144.dp)
                    ) {
                        SingleLineText(stringResource(Res.string.dismiss))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CommentDialogPreview() {
    MainAppTheme(themeColors = LightColors) {
        val comments = listOf(
            Comment(
                id = 1,
                verse = 1,
                chapter = 1,
                comment = "This is the first comment",
                bookId = 1,
                created = "",
                modified = ""
            ),
            Comment(
                id = 2,
                verse = 1,
                chapter = 1,
                comment = "This is the second comment",
                bookId = 1,
                created = "",
                modified = ""
            )
        )

        CommentDialog(
            title = "Genesis 1:1",
            comments = comments,
            onSave = {},
            onDelete = {},
            onDismiss = {}
        )
    }
}