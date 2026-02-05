package com.example.vocabularyproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import com.example.vocabularyproject.ui.dialogs.AddWordDialog
import com.example.vocabularyproject.ui.dialogs.ListDialog
import com.example.vocabularyproject.util.EditField

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar
@Composable
fun DaftarKataScreen(
    iWViewModel: InputWordViewModel = hiltViewModel()
)
{

    val translations by iWViewModel.translationsM.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showListDialog by remember { mutableStateOf(false) }

    var selectedITables by remember { mutableStateOf<List<IndonesianWordsTable>?>(null) }
    var selectedWord by remember { mutableStateOf("") }
    var selectedEId by remember { mutableStateOf(0L) }
    var editField by remember { mutableStateOf<EditField?>(null) }


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
                    onEWordClick = {
                        iWViewModel.setCurrentId(wordItem.english.eId)
                        selectedEId = wordItem.english.eId
                        selectedWord = wordItem.english.eWord
                        editField = EditField.E_WORD
                        showDialog = true
                    },
                    onEDefinitionClick = {
                        iWViewModel.setCurrentId(wordItem.english.eId)
                        selectedEId = wordItem.english.eId
                        selectedWord = wordItem.english.definition
                        editField = EditField.DEFINITION
                        showDialog = true
                    },
                    onIWordsClick = {
                        iWViewModel.setCurrentId(wordItem.english.eId)
                        selectedITables=wordItem.indonesianWords
                        showListDialog=true
                    },
                    onDeleteClick = {
                        iWViewModel.deleteEWord(wordItem.english.eId)
                        iWViewModel.deleteIWords(wordItem.indonesianWords)
                    }
                )
            }
        }
        if (showDialog) {
            AddWordDialog(
                id = selectedEId ,
                initialWord = selectedWord,
                onDismiss = { showDialog = false },
                onAddClick = { id,word ->
                    Log.i("DAFTARKATASCREEN","Show dialog id: $id")
                    if (editField==EditField.E_WORD)   iWViewModel.updateEWord(id!!,word)
                    else if (editField==EditField.DEFINITION) iWViewModel.updateDefinition(id!!,word)
                    else if (editField==EditField.I_WORDS) {
                        iWViewModel.updateIWord(id!!,word)
                        showListDialog=true
                    }
                    showDialog = false
                }
            )
        }
        if (showListDialog) {
            ListDialog(
                translations = translations,
                onDismiss = { showListDialog = false },
                onWordClick = { item->
                    selectedWord=item.iWord
                    selectedEId = item.iId
                    editField= EditField.I_WORDS
                    showDialog=true
                },
                onDelete = {item->
                    iWViewModel.deleteIWord(item.iId)
                },{
                    editField= EditField.I_WORDS
                    selectedEId=0L
                    Log.i("DAFTARKATASCREEN","Set SelectedEId to: $selectedEId")
                    showDialog=true
                }
            )
        }
    }
}