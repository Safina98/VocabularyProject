package com.example.vocabularyproject.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.vocabularyproject.ui.theme.ThulianPink
import com.example.vocabularyproject.ui.theme.amarath
import com.example.vocabularyproject.ui.theme.brookGreen
import com.example.vocabularyproject.ui.theme.brookGreenLight
import com.example.vocabularyproject.ui.theme.chalk

enum class EditField {
    E_WORD,
    DEFINITION,
    I_WORDS
}

val cardGradientColors = listOf(brookGreen, brookGreenLight, brookGreen)
val cardGradientBrush = Brush.horizontalGradient(colors = cardGradientColors)
val buttonGradientClolor = listOf(amarath, amarath, amarath)
