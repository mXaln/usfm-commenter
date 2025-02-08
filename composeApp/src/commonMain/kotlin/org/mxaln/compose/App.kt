package org.mxaln.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.dependencies.MyViewModel
import org.mxaln.database.Comment

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = koinViewModel<MyViewModel>()
        val coroutineScope = rememberCoroutineScope()

        var enterUrl by remember { mutableStateOf("") }
        var enterFileName by remember { mutableStateOf("") }
        var comments by remember { mutableStateOf(emptyList<Comment>()) }

        val filePickerLauncher = rememberFilePickerLauncher(
            type = PickerType.File(),
            title = "Select a USFM file"
        ) { result ->
            result?.let {
                viewModel.importUsfm(it.toKmpFile())
            }
        }

        LaunchedEffect(key1 = viewModel.comments) {
            viewModel.comments.collectLatest {
                comments = it
            }
        }

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
                    value = enterUrl,
                    onValueChange = { enterUrl = it },
                    label = { Text("Enter Url") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.downloadUsfm(enterUrl)
                        enterUrl = ""
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

            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = enterFileName,
                    onValueChange = { enterFileName = it },
                    label = { Text("Enter file name") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.readUsfm(enterFileName)
                        enterFileName = ""
                    },
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text("Read USFM")
                }
            }

            Text(
                viewModel.usfmOutput.value,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}