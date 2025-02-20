package org.mxaln.compose.ui.control

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mxaln.compose.ui.theme.LightColors
import org.mxaln.compose.ui.theme.MainAppTheme
import org.mxaln.database.Book

@Composable
fun BookCard(
    book: Book,
    onSelect: (Book) -> Unit,
    onDelete: (Book) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
            .clickable { onSelect(book) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(end = 16.dp)
        ) {
            SingleLineText(
                text = book.name,
                modifier = Modifier.padding(16.dp).weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleLineText(
                    text = book.slug,
                    modifier = Modifier.padding(16.dp)
                )
                Box(
                    modifier = Modifier
                        .clickable { onDelete(book) }
                        .widthIn(min = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun BookCardPreview() {
    MainAppTheme(themeColors = LightColors) {
        BookCard(
            book = Book(1, "test", "test", "test"),
            onSelect = {},
            onDelete = {}
        )
    }
}