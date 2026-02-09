package com.example.vocabularyproject.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vocabularyproject.util.cardGradientColors


@Composable
fun WordListCard(eWord: String,
                 defition: String,
                 translations: List<String>,
                 onEWordClick: () -> Unit,
                 onEDefinitionClick: () -> Unit,
                 onIWordsClick: () -> Unit,
                 onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
        // Note: Card uses 'colors = CardDefaults.cardColors(...)' for background normally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.horizontalGradient(colors = cardGradientColors))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // Apply the gradient here to cover the whole internal area
                    .background(brush = Brush.horizontalGradient(colors = cardGradientColors))
                    .padding(12.dp), // Add padding so text doesn't touch the edges
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Header: Word and Definition
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.clickable { onEWordClick() },
                        text = "$eWord :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.clickable { onEDefinitionClick() },
                        text = defition.lowercase(),
                        style = MaterialTheme.typography.bodySmall,
                        // If the definition is long, it will wrap automatically here
                    )
                }

                // Translations section
                FlowRow(
                    modifier = Modifier.fillMaxWidth().clickable { onIWordsClick() },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)

                ) {
                    translations.forEach { indo ->
                        Text(
                            text = "• $indo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }

            }
// Trash Can Icon
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Word",
                    tint = Color.DarkGray // Or any color that fits your gradient
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ContactCardPreview(){
    WordListCard(
        "Jane Doe",
        "Unknown Woman\n unidentified women\n common unimportan woman",
        listOf("Ri rima","Tidak di kenal"),
        {},
        {},
        {},
        {}
    )
}
