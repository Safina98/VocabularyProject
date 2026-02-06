package com.example.vocabularyproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.ui.theme.VocabularyProjectTheme
import com.example.vocabularyproject.util.buttonGradientClolor

@Composable
fun HomeScreen(
    onInputKataClick: () -> Unit,
    onDaftarKataClick: () -> Unit,
    onPermainanClick:()->Unit
    ){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
    ) {
        HomeScreenButtonStyles("Input Kata",
            onClick  = onInputKataClick,
            modifier = Modifier.fillMaxWidth(),
            gradientColors = buttonGradientClolor
        )

        HomeScreenButtonStyles("Mulai Permainana",
            onClick =onPermainanClick,
            modifier = Modifier.fillMaxWidth(),
            gradientColors = buttonGradientClolor
        )
        HomeScreenButtonStyles("Lihat Daftar Kata",
            onClick = onDaftarKataClick,
            modifier = Modifier.fillMaxWidth(),
            gradientColors = buttonGradientClolor
        )

    }

}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    // Wrap it in your App Theme so it looks correct
    VocabularyProjectTheme() {
        HomeScreen({},{},{})
    }
}