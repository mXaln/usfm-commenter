package org.mxaln.compose.ui.control

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mxaln.compose.data.Chapter
import org.mxaln.compose.data.Verse
import org.mxaln.database.Comment

@Composable
fun ChapterCard(
    chapter: Chapter,
    comments: List<Comment>,
    onVerseClick: (verse: Verse) -> Unit
) {
    Column {
        Text(
            text = "Chapter: ${chapter.number}",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chapter.verses.map { verse ->
                val verseComments = comments.filter { it.verse.toInt() == verse.number }
                Text(
                    text = "${verse.number}. ${verse.text}",
                    color = if (verseComments.isNotEmpty()) Color.Red else Color.DarkGray,
                    modifier = Modifier.clickable {
                        onVerseClick(verse)
                    }
                )
            }
        }
    }
}
