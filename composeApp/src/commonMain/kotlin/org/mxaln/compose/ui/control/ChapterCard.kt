package org.mxaln.compose.ui.control

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mxaln.compose.data.Chapter

@Composable
fun ChapterCard(chapter: Chapter) {
    Column {
        Text(
            text = "Chapter: ${chapter.number}",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))
        chapter.verses.map { verse ->
            Row {
                Text("${verse.number}. ${verse.text}")
            }
        }
    }
}
