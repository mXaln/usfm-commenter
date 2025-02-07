package org.mxaln.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.dependencies.MyViewModel
import org.mxaln.database.Comment

@Composable
@Preview
fun App() {
    MaterialTheme {
        var enterVerse by remember { mutableStateOf("") }
        var enterChapter by remember { mutableStateOf("") }
        var enterBook by remember { mutableStateOf("") }
        var enterComment by remember { mutableStateOf("") }

        val viewModel = koinViewModel<MyViewModel>()

        var comments by remember { mutableStateOf(emptyList<Comment>()) }

        val onAddComment: (String, String, String, String) -> Unit = { verse, chapter, book, comment ->
            if (verse.isNotEmpty() && chapter.isNotEmpty() && book.isNotEmpty() && comment.isNotEmpty()) {
                viewModel.addComment(verse, chapter, book, comment)
            }
        }

        LaunchedEffect(key1 = viewModel.comments) {
            viewModel.comments.collectLatest {
                comments = it
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn {
                itemsIndexed(comments) { _, comment ->
                    Row {
                        Text("verse ${comment.verse}, ")
                        Text("chapter ${comment.chapter}, ")
                        Text("book ${comment.book}: ")
                        Text(comment.comment)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = enterVerse,
                    onValueChange = { enterVerse = it },
                    label = { Text("Enter Verse") },
                )
                OutlinedTextField(
                    value = enterChapter,
                    onValueChange = { enterChapter = it },
                    label = { Text("Enter Chapter") },
                )
                OutlinedTextField(
                    value = enterBook,
                    onValueChange = { enterBook = it },
                    label = { Text("Enter Book") },
                )
                OutlinedTextField(
                    value = enterComment,
                    onValueChange = { enterComment = it },
                    label = { Text("Enter Comment") },
                )
            }

            Button(onClick = {
                onAddComment(
                    enterVerse,
                    enterChapter,
                    enterBook,
                    enterComment
                )
            }) {
                Text("Click me!")
            }

            Text(viewModel.test.value)
        }
    }
}