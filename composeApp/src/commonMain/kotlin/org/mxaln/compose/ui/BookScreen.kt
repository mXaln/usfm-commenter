package org.mxaln.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import org.mxaln.compose.ui.control.ChapterCard
import org.mxaln.database.Book

data class BookScreen(private val book: Book) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<BookViewModel>()

        val chapters by viewModel.chapters.collectAsStateWithLifecycle(emptyList())

        LaunchedEffect(key1 = book) {
            viewModel.parseBook(book)
        }

        Scaffold(
            topBar = { TopNavigationBar("${book.name} (${book.slug})") }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(chapters) { chapter ->
                    ChapterCard(chapter)
                }
            }
        }
    }
}