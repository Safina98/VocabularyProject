package com.example.vocabularyproject.ui.screens

import DaftarKataAdapter
import EWordListener
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vocabularyproject.viewmodels.InputWordViewModel
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.ui.dialogs.AddWordDialog
import com.example.vocabularyproject.ui.dialogs.ListDialog
import com.example.vocabularyproject.ui.theme.amarath
import com.example.vocabularyproject.util.EditField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.vocabularyproject.ui.widgetstyles.WordListCard


@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar
@Composable
fun DaftarKataScreen(
    iWViewModel: InputWordViewModel = hiltViewModel()
)
{

    val translations by iWViewModel.translationsM.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showListDialog by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf("") }
    var selectedEId by remember { mutableStateOf(0L) }
    var editField by remember { mutableStateOf<EditField?>(null) }


    val filteredWords by iWViewModel.filteredWords.collectAsState()
    val searchQuery by iWViewModel.searchQuery.collectAsState()

    // We only keep UI-specific state (like is the bar open) in the Screen
    var isSearchActive by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val items = (1..50).map { "Item $it" }

    // Filter the list based on query
    Scaffold(
        containerColor = Color.Transparent,
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
                    // Change the color of the text and icons here
                    containerColor = amarath,
                    titleContentColor = Color.White
                )

            )
        }
    ) { innerPadding -> // This innerPadding is crucial!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = rememberLazyListState()
            ) {
                items(
                    items = filteredWords,
                    key = { it.english.eId }   // 🔥 Important for smooth performance
                ) { wordItem ->

                    WordListCard(
                        wordTransa = wordItem,
                        index = 0, // or remove if not needed
                        onEWordClick = EWordListener { wtModel ->
                            iWViewModel.setCurrentId(wtModel.english.eId)
                            selectedEId = wtModel.english.eId
                            selectedWord = wtModel.english.eWord
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
                            showListDialog = true
                        },
                        onDeleteClick = {
                            iWViewModel.deleteEWord(wordItem.english.eId)
                            iWViewModel.deleteIWords(wordItem.indonesianWords)
                        }
                    )
                }
            }


            // Use LazyColumn instead of Column + forEach for better performance
            // 5. Wrap your lambdas in remember to prevent them from being recreated on every scroll
            val onEWordClickRemembered = remember(selectedEId) {
                { wordItem: WordTranslationModel ->
                    iWViewModel.setCurrentId(wordItem.english.eId)
                    selectedEId = wordItem.english.eId
                    selectedWord = wordItem.english.eWord
                    editField = EditField.E_WORD
                    showDialog = true
                }
            }
//            AndroidView(
//                factory = { context ->
//                    RecyclerView(context).apply {
//                        layoutManager = LinearLayoutManager(context)
//                        adapter = DaftarKataAdapter(
//                            onEWordClick = EWordListener { wtModel ->
//                                showDialog = true
//                                selectedEId = wtModel.english.eId
//                                selectedWord = wtModel.english.eWord
//                                editField = EditField.E_WORD
//                            },
//                            {}, {}, {}
//                        )
//                    }
//                },
//                update = { recyclerView ->
//                    (recyclerView.adapter as? DaftarKataAdapter)
//                        ?.submitList(filteredWords)
//                },
//                modifier = Modifier.fillMaxSize()
//            )


// ... Repeat for other clicks or use a single event handler
            // Filter the list based on query

            // Use LazyColumn instead of Column + forEach for better performance
//        LazyColumn(
//            state = listState,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding), // Prevents toolbar from overlapping content
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
//        ) {
//            items(
//                items = filteredWords,
//                key = { it.english.eId },
//                contentType = { "word_item" }
//            ) { wordItem ->
//
//                WordListCard(
//                    eWord = wordItem.english.eWord,
//                    defition = wordItem.english.definition,
//                    translations = listOf(""),
//                    onEWordClick = { onEWordClickRemembered(wordItem) },
//                    onEDefinitionClick = {
//                        iWViewModel.setCurrentId(wordItem.english.eId)
//                        selectedEId = wordItem.english.eId
//                        selectedWord = wordItem.english.definition
//                        editField = EditField.DEFINITION
//                        showDialog = true
//                    },
//                    onIWordsClick = {
//                        iWViewModel.setCurrentId(wordItem.english.eId)
//                        showListDialog = true
//                    },
//                    onDeleteClick = {
//                        iWViewModel.deleteEWord(wordItem.english.eId)
//                        iWViewModel.deleteIWords(wordItem.indonesianWords)
//                    }
//                )
//            }
//        }
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