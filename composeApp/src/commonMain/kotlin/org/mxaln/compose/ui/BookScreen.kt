package org.mxaln.compose.ui

import androidx.compose.foundation.layout.Arrangement
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
import cafe.adriel.voyager.koin.koinScreenModel
import org.koin.core.parameter.parametersOf
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.compose.ui.control.ChapterCard
import org.mxaln.compose.ui.dialog.CommentDialog
import org.mxaln.compose.ui.dialog.ProgressDialog
import org.mxaln.database.Book

data class BookScreen(private val book: Book) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<BookViewModel> {
            parametersOf(book)
        }

        val chapters by viewModel.chapters.collectAsStateWithLifecycle(emptyList())
        val comments by viewModel.comments.collectAsStateWithLifecycle(emptyList())

        var selectedChapter by remember { mutableStateOf<Chapter?>(null) }
        var selectedVerse by remember { mutableStateOf<Verse?>(null) }

        Scaffold(
            topBar = { TopNavigationBar("${book.name} [${book.slug}]") }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(chapters) { chapter ->
                    ChapterCard(
                        chapter  = chapter,
                        comments = comments.filter { it.chapter.toInt() == chapter.number }
                    ) { verse ->
                        selectedChapter = chapter
                        selectedVerse = verse
                    }
                }
            }

            selectedChapter?.let { chapter ->
                selectedVerse?.let { verse ->
                    CommentDialog(
                        title = "${book.name} ${chapter.number}:${verse.number}",
                        comments = comments.filter {
                            chapter.number.toLong() == it.chapter &&
                                    verse.number.toLong() == it.verse
                        },
                        onSave = { comment ->
                            viewModel.addComment(
                                chapter = chapter,
                                verse = verse,
                                comment = comment
                            )
                        },
                        onDelete = { comment ->
                            viewModel.deleteComment(comment)
                        },
                        onDismiss = {
                            selectedChapter = null
                            selectedVerse = null
                        }
                    )
                }
            }

            viewModel.progress?.let {
                ProgressDialog(getLocalizedString(it))
            }
        }
    }
}