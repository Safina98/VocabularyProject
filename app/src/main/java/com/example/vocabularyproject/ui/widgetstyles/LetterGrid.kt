package com.example.vocabularyproject.ui.widgetstyles
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vocabularyproject.ui.theme.amarath
import com.example.vocabularyproject.ui.theme.brookGreen
import com.example.vocabularyproject.ui.theme.chalk

@Composable
fun LetterGrid(
    selectedLetter: String,
    onLetterSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 7
) {
    val letters = ('A'..'Z').map { it.toString() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        letters.chunked(columns).forEach { rowLetters ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowLetters.forEach { letter ->
                    val isSelected = letter == selectedLetter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) amarath
                                else chalk
                            )
                            .border(
                                width = 1.dp,
                                color =  if (isSelected) amarath else brookGreen,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onLetterSelected(letter) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
                // Fill the empty slots in the last row so the cells keep the same width
                repeat(columns - rowLetters.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}