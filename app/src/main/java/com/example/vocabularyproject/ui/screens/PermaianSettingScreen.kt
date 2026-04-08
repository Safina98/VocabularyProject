package com.example.vocabularyproject.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieConstants
import com.example.vocabularyproject.ui.theme.Typography
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.ui.widgetstyles.RadioButtonGroup
import com.example.vocabularyproject.ui.widgetstyles.TransparentButton
import com.example.vocabularyproject.util.OpsiPermaian
import com.example.vocabularyproject.util.buttonGradientClolor
import com.example.vocabularyproject.util.cardGradientBrush
import com.example.vocabularyproject.util.opsiList
import com.example.vocabularyproject.viewmodels.GameViewModel
import com.example.vocabularyproject.viewmodels.InputWordViewModel

@Composable
fun PermainanSettingScreen(
    onMulaiPermainanClick: () -> Unit = {},
    gViewModel: GameViewModel
){
    // add new bolean column to english word database
    // add a list of chekcbox to pick which batch of words to choose to play the game
    //Game option
    //  1. all words
    //  2. the words that are marked true
    //  3. batch of words
    //var selected by remember { mutableStateOf("") }
    val selectedOption by gViewModel.selectedOption.collectAsState()
    val selectedBatch by gViewModel.selectedBatch.collectAsState()
    var qty by remember { mutableStateOf("20") }

    val batchQuantity by gViewModel.batchQuantity.collectAsState()
    val batchList by gViewModel.batchList.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .clickable { },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(brush = cardGradientBrush)
                    .padding(16.dp)
                    .wrapContentHeight(),

                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ){
                Text(modifier = Modifier.padding(24.dp),
                    style = Typography.titleLarge,
                    text = "Pilih Mode")
                RadioButtonGroup(
                    options = opsiList,
                    selectedOption = selectedOption,
                    onOptionSelected = { gViewModel.onOptionMenuChange(it) }
                )

                if (selectedOption== OpsiPermaian.opt3batchkata) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = qty, // Display current state
                            onValueChange = { newText -> qty=newText }, // Update state when typing
                            label = { Text("Jumlah kata") },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Jumlah Kata") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TransparentButton(
                            "Ok",
                            modifier = Modifier.padding(vertical = 24.dp),
                            onClick ={ gViewModel.setBatchQuantity(qty) } ,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 150.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            RadioButtonGroup(
                                options = batchList,
                                selectedOption = selectedBatch,
                                onOptionSelected = { gViewModel.onBatchListSelected(it) }
                            )
                        }
                    }
                }
                HomeScreenButtonStyles(
                    "Mulai Permainan",
                    onClick ={
                        if (selectedOption.isNotEmpty()){
                            if (selectedOption== OpsiPermaian.opt3batchkata && selectedBatch.isEmpty()) {

                            }else{
                                gViewModel.filterWords()
                                onMulaiPermainanClick()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = buttonGradientClolor
                )
                Log.i("OpsiList","$opsiList")
            }
        }
    }
}

