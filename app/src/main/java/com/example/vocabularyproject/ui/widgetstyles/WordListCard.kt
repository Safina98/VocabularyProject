package com.example.vocabularyproject.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp




@Composable
fun WordListCard(eWord: String, defition: String, translations: List<String>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
        // Note: Card uses 'colors = CardDefaults.cardColors(...)' for background normally
    ) {
        val gradientColors = listOf(Color(0xFFB2DFDB), Color(0xFFF8BBD0), Color(0xFFFFCDD2))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Apply the gradient here to cover the whole internal area
                .background(brush = Brush.horizontalGradient(colors = gradientColors))
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
                    text = "$eWord :",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = defition,
                    style = MaterialTheme.typography.bodyLarge,
                    // If the definition is long, it will wrap automatically here
                )
            }

            // Translations section
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
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
    }
}

@Preview(showBackground = true)
@Composable
fun ContactCardPreview(){
    WordListCard("Jane Doe","Unknown Woman\n unidentified women\n common unimportan woman",listOf("Ri rima","Tidak di kenal"),{})

}