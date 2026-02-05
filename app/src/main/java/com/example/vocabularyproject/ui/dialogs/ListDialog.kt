package com.example.vocabularyproject.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vocabularyproject.database.tables.IndonesianWordsTable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDialog(
    translations: List<IndonesianWordsTable>,
    onDismiss: () -> Unit,
    onWordClick: (IndonesianWordsTable) -> Unit,
    onDelete:(IndonesianWordsTable)->Unit,
    onAdd:()->Unit,

) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        confirmButton = {
            TextButton(onClick = onAdd) {
                Text("Add")
            }
        },
        title = {
            Text(
                text = "Translations",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Tap a word to edit it specifically:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                // FlowRow handles wrapping automatically if there are many words
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    translations.forEach { item ->
                        InputChip(
                            selected = false, // Or manage state if needed
                            onClick = { onWordClick(item) },
                            label = { Text(item.iWord) },
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Delete",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            onDelete(item)
                                        },
                                    tint = Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun ListDialogPreview() {
    // 1. Create mock data
    val mockTranslations = listOf(
        IndonesianWordsTable(iId = 1, iWord = "Makan"),
        IndonesianWordsTable(iId = 2, iWord = "Menyantap"),
        IndonesianWordsTable(iId = 3, iWord = "Mengunyah")
    )

    // 2. Wrap in a Theme if you're using Material 3
    MaterialTheme {
        ListDialog(
            translations = mockTranslations,
            onDismiss = { /* Do nothing in preview */ },
            onWordClick = { selectedWord ->
                println("Clicked on: ${selectedWord.iWord}")
            },
            onDelete = {
                println("Deleted: ${it.iWord}")
            },{

            }
        )
    }
}