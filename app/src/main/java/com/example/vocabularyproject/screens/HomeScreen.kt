package com.example.vocabularyproject.screens

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

@Composable
fun HomeScreen( onInputKataClick: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
    ) {
        HomeScreenButtonStyles("Input Kata",
            onClick  = onInputKataClick,
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(Color(0xFF1C0838),Color(0xFF1A7067), Color(0xFF4C6461))
        )

        HomeScreenButtonStyles("Mulai Permainana",
            onClick = {
                //Navigate to TebakKataScreen
                Log.i("HomeScreen","Mulai Permainan Cliked")
                      },
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(Color(0xFF1C0838),Color(0xFF1A7067), Color(0xFF4C6461))
        )
        HomeScreenButtonStyles("Lihat Daftar Kata",
            onClick = {
                //Navigate to DaftarKata Screen
                Log.i("HomeScreen","Lihat Daftar Kata clicked")
                      },
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(Color(0xFF1C0838),Color(0xFF1A7067), Color(0xFF4C6461))
        )

    }

}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    // Wrap it in your App Theme so it looks correct
    VocabularyProjectTheme() {
        HomeScreen({})
    }
}