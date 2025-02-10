package org.mxaln.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.ui.control.BookCard
import org.mxaln.compose.ui.dialog.ErrorDialog

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()

        val navigator = LocalNavigator.currentOrThrow

        var usfmUrl by remember { mutableStateOf("") }
        val books by viewModel.books.collectAsStateWithLifecycle(emptyList())

        val filePickerLauncher = rememberFilePickerLauncher(
            type = PickerType.File(),
            title = "Select a USFM file"
        ) { result ->
            result?.let {
                viewModel.importUsfm(it.toKmpFile())
            }
        }

        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = usfmUrl,
                        onValueChange = { usfmUrl = it },
                        label = { Text("Enter Url") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            viewModel.downloadUsfm(usfmUrl)
                            usfmUrl = ""
                        },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text("Download USFM")
                    }
                    Text("OR")
                    Button(
                        onClick = { filePickerLauncher.launch() },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text("Import USFM File")
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(books) { book ->
                        BookCard(
                            book = book,
                            onSelect = { navigator.push(BookScreen(book)) },
                            onDelete = { viewModel.deleteBook(book) }
                        )
                    }
                }
            }

            if (viewModel.error.isNotEmpty()) {
                ErrorDialog(error = viewModel.error, onDismiss = { viewModel.error = "" })
            }
        }
    }
}