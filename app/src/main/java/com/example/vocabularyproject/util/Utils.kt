package com.example.vocabularyproject.util

import androidx.compose.ui.graphics.Brush
import com.example.vocabularyproject.ui.theme.amarath
import com.example.vocabularyproject.ui.theme.brookGreen
import com.example.vocabularyproject.ui.theme.brookGreenLight

enum class EditField {
    E_WORD,
    DEFINITION,
    I_WORDS
}

object OpsiPermaian {
    const val opt1semuakata= "Semua kata"
   // const val opt2katadipilih = "kata tertentu"
    const val opt2batchkata = "batch kata"

    const val opt4batchkata = "Alphabet"
}
object BahasaPermaian {
    const val opt1EnglishToIndonesian= "Inggris ke Indonesia"
    // const val opt2katadipilih = "kata tertentu"
    const val opt2IndonesianToEnglish = "Indonesia ke Inggris"
}
val bahasaList= BahasaPermaian::class.java.declaredFields.filter { it.type==String::class.java }.map {it.get(null) as String}
val opsiList = OpsiPermaian::class.java.declaredFields
    .filter { it.type == String::class.java }
    .map { it.get(null) as String }
val cardGradientColors = listOf(brookGreen, brookGreenLight, brookGreen)
val cardGradientBrush = Brush.horizontalGradient(colors = cardGradientColors)
val buttonGradientClolor = listOf(amarath, amarath, amarath)
