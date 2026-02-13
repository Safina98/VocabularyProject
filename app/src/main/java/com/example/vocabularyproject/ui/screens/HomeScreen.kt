package com.example.vocabularyproject.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vocabularyproject.ui.theme.VocabularyProjectTheme
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.util.buttonGradientClolor

@Composable
fun HomeScreen(
    onInputKataClick: () -> Unit,
    onDaftarKataClick: () -> Unit,
    onPermainanClick: () -> Unit,
    onSettingClick: () -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HomeScreenButtonStyles(
                "Input Kata",
                onClick = onInputKataClick,
                modifier = Modifier.fillMaxWidth(),
                gradientColors = buttonGradientClolor
            )

            HomeScreenButtonStyles(
                "Mulai Permainana",
                onClick = onPermainanClick,
                modifier = Modifier.fillMaxWidth(),
                gradientColors = buttonGradientClolor
            )

            HomeScreenButtonStyles(
                "Lihat Daftar Kata",
                onClick = onDaftarKataClick,
                modifier = Modifier.fillMaxWidth(),
                gradientColors = buttonGradientClolor
            )
        }

        // ⚙️ Settings Icon Bottom-End
        IconButton(
            onClick = onSettingClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,     // distance from right edge
                    bottom = 40.dp   // distance from bottom
                )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}


@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    // Wrap it in your App Theme so it looks correct
    VocabularyProjectTheme() {
        HomeScreen({},{},{},{})
    }
}