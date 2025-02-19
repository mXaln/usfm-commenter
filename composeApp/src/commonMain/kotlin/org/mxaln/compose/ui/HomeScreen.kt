package org.mxaln.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.ui.control.BookCard
import org.mxaln.compose.ui.control.ImportFloatingMenu
import org.mxaln.compose.ui.control.MenuItem
import org.mxaln.compose.ui.dialog.ConfirmDialog
import org.mxaln.compose.ui.dialog.ErrorDialog
import org.mxaln.compose.ui.dialog.ImportApiDialog
import org.mxaln.compose.ui.dialog.ProgressDialog
import org.mxaln.compose.ui.theme.CommonColors
import usfmcommenter.composeapp.generated.resources.Res
import usfmcommenter.composeapp.generated.resources.select_usfm_file

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        val progress by viewModel.progress
        var error by viewModel.error
        var showBookDialog by viewModel.showBookDialog
        var confirmMessage by viewModel.confirmAction

        val fabMenuExpanded = remember { mutableStateOf(false) }

        val books by viewModel.books.collectAsStateWithLifecycle(emptyList())
        val apiBooks by viewModel.apiBooks.collectAsStateWithLifecycle(emptyList())

        val filePickerLauncher = rememberFilePickerLauncher(
            type = PickerType.File(),
            title = stringResource(Res.string.select_usfm_file)
        ) { result ->
            result?.let {
                viewModel.importUsfm(it.toKmpFile())
            }
        }

        Scaffold(
            floatingActionButton = {
                ImportFloatingMenu(fabMenuExpanded) { item ->
                    when (item) {
                        MenuItem.IMPORT_FILE -> filePickerLauncher.launch()
                        MenuItem.IMPORT_CLOUD -> viewModel.showImportBookDialog()
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 116.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

            // overlay to hide expanded fab menu when clicked
            if (fabMenuExpanded.value) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(CommonColors.SemiTransparent)
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) { fabMenuExpanded.value = false }
                )
            }

            if (showBookDialog) {
                ImportApiDialog(
                    books = apiBooks,
                    onItemClicked = { viewModel.downloadUsfm(it) },
                    onDismiss = { showBookDialog = false }
                )
            }

            confirmMessage?.let {
                ConfirmDialog(
                    message = stringResource(it.message),
                    onConfirm = it.onConfirm,
                    onCancel = it.onCancel,
                    onDismiss = { confirmMessage = null }
                )
            }

            error?.let {
                ErrorDialog(
                    error = getLocalizedString(it),
                    onDismiss = { error = null }
                )
            }

            progress?.let {
                ProgressDialog(getLocalizedString(it))
            }
        }
    }
}