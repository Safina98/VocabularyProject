package com.example.vocabularyproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyproject.database.VocabularyRepository
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputWordViewModel @Inject constructor( private val repository: VocabularyRepository) :
    ViewModel()
{
    val iWordsListM= MutableStateFlow<List<String>>(emptyList())
    val iWordsListL= iWordsListM.asStateFlow()
    val eWordM=  MutableStateFlow<String>("")
    val definitionM=MutableStateFlow<String>("")

    val englishWords: StateFlow<List<EnglishWordsTable>> =
        repository.getAllEnglishWords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val indonesianWords: StateFlow<List<IndonesianWordsTable>> =
        repository.getAllIndonesianWords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val correlatedId: StateFlow<List<CorrelatedWord>> =
        repository.getAllCorrelatedId()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addWord(word:String) {
        iWordsListM.value=iWordsListM.value+word
    }

    fun saveWord() {
        viewModelScope.launch {
            val eWTable= EnglishWordsTable()
            eWTable.eId=System.currentTimeMillis()
            eWTable.eWord=eWordM.value
            eWTable.definition=definitionM.value
            repository.insertEnglishWord(eWTable)

            var i =1
            iWordsListM.value.forEach {
                val iWTable= IndonesianWordsTable()
                val crTable= CorrelatedWord()
              Log.i("InputWordVm","$i $it")
              i+=1
                iWTable.iId=System.currentTimeMillis()
                iWTable.iWord=it
                repository.insertIndonesianWord(iWTable)
                crTable.eId=eWTable.eId
                crTable.iId=iWTable.iId
                repository.insertCorrelatedWord(crTable)
                delay(1)
            }
        }
    }

    fun resetValues(){
        iWordsListM.value= emptyList()
        eWordM.value=""
        definitionM.value=""
    }
}