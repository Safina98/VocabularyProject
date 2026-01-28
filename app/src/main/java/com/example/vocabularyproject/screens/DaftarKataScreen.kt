package com.example.vocabularyproject.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vocabularyproject.ui.widgetstyles.WordListCard
import com.example.vocabularyproject.viewmodels.InputWordViewModel

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar
@Composable
fun DaftarKataScreen(iWViewModel: InputWordViewModel = hiltViewModel()) {
    val translatedWords by iWViewModel.wtModelList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Vocabulary List", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding -> // This innerPadding is crucial!

        // Use LazyColumn instead of Column + forEach for better performance
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Prevents toolbar from overlapping content
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            items(translatedWords) { wordItem ->
                WordListCard(
                    eWord = wordItem.english.eWord,
                    defition = wordItem.english.definition,
                    translations = wordItem.indonesianWords.map { it.iWord },
                    onClick = { Log.i("DaftarKata", "Card Clicked") }
                )
            }
        }
    }
}