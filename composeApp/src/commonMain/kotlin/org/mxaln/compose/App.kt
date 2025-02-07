package org.mxaln.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.dependencies.MyViewModel
import org.mxaln.database.Comment

@Composable
@Preview
fun App() {
    MaterialTheme {
        var text by remember { mutableStateOf<String?>(null) }
        val viewModel = koinViewModel<MyViewModel>()

        var comments by remember { mutableStateOf(emptyList<Comment>()) }
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(key1 = viewModel.comments) {
            viewModel.comments.collectLatest {
                comments = it
            }
        }

        LazyColumn {
            itemsIndexed(comments) { _, comment ->
                Text(comment.comment)
            }
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {

            }) {
                Text("Click me!")
            }
            if (text != null) {
                Dialog(
                    onDismissRequest = { text = null },
                    content = {
                        Text(text!!)
                    }
                )
            }
        }
    }
}