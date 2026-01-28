package com.example.vocabularyproject.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar
@Composable
fun DaftarKataScreen(iWViewModel: InputWordViewModel = hiltViewModel()) {
    val translatedWords by iWViewModel.wtModelList.collectAsState()
    val filteredWords by iWViewModel.filteredWords.collectAsState()
    val searchQuery by iWViewModel.searchQuery.collectAsState()

    // We only keep UI-specific state (like is the bar open) in the Screen
    var isSearchActive by remember { mutableStateOf(false) }

    // Filter the list based on query

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { iWViewModel.onSearchQueryChange(it) },
                            placeholder = { Text("Search word...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                // Set all container states to transparent
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                // Set indicators to transparent to hide the bottom line
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    } else {
                        Text("Vocabulary List", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    // This adds the icon to the right side
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) iWViewModel.onSearchQueryChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Words"
                        )
                    }
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
            items(filteredWords) { wordItem ->
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