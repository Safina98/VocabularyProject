package com.example.vocabularyproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vocabularyproject.ui.dialogs.AddWordDialog
import com.example.vocabularyproject.ui.widgetstyles.AddButton
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.viewmodels.InputWordViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.vocabularyproject.ui.widgetstyles.TransparentButton


@Composable
fun InputKataScreen(
    iWViewModel: InputWordViewModel = hiltViewModel(),
    onDaftarKataClick: () -> Unit = {}
    ) {
    var showDialog by remember { mutableStateOf(false) }

    val words by iWViewModel.iWordsListL.collectAsState()
    val eWord by iWViewModel.eWordM.collectAsState()
    val definition by iWViewModel.definitionM.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
    ) {
        OutlinedTextField(
            value = eWord, // Display current state
            onValueChange = { newText -> iWViewModel.eWordM.value = newText }, // Update state when typing
            label = { Text("Word") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("word") }
        )
        OutlinedTextField(
            value = definition, // Display current state
            onValueChange = { newText ->iWViewModel.definitionM.value = newText }, // Update state when typing
            label = { Text("Definition") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("word") }
        )
        //add a small add button
        AddButton({ showDialog = true
        },
            modifier = Modifier.padding(16.dp)
                .align(Alignment.End) //
        )
        words.forEach {
            Text(text = it)
        }
        //create a pop up when the button click
        //the word from the pop up shows as list of text view in this fragment
        HomeScreenButtonStyles("Save",
            onClick = {
                iWViewModel.saveWord()

            },
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(Color(0xFF1C0838),Color(0xFF1A7067), Color(0xFF4C6461))
        )
        TransparentButton(
            "Lihat Daftar Kata",
            onClick = onDaftarKataClick,
            modifier = Modifier.fillMaxWidth()
        )

        if (showDialog) {
            AddWordDialog(
                null,null,
                onDismiss = { showDialog = false },
                onAddClick = { _,word ->
                    iWViewModel.addWord(word)
                    //Log.i("InputKataScreen", "Added word: $word")
                    showDialog = false
                }
            )
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun InputKataScreenPreview() {
//    InputKataScreen(hiltViewModel(),{})
//}
