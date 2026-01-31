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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputWordViewModel @Inject constructor( private val repository: VocabularyRepository) :
    ViewModel() {
    val iWordsListM= MutableStateFlow<List<String>>(emptyList())
    val iWordsListL= iWordsListM.asStateFlow()
    val eWordM=  MutableStateFlow<String>("")
    val definitionM=MutableStateFlow<String>("")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val englishWords: StateFlow<List<EnglishWordsTable>> =
        repository.getAllEnglishWords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val wtModelList =
        repository.getWordTranslationList()
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
        Log.i("InputKataScreen","VM  Add Word ${iWordsListM.value}")
    }

    fun updateEWord(eId:Long, eWord:String){
        viewModelScope.launch {
            repository.updateEWordById(eId,eWord)
        }
    }
    fun updateDefinition(id:Long, definition:String){
        viewModelScope.launch {
            repository.updateDefinitionById(id,definition)
        }
    }
    fun updateIWord(iId:Long, iWord:String){
        viewModelScope.launch {
            Log.i("DaftarKataScreen","View Model $iId $iWord")
            repository.updateIWord(iId,iWord)
        }
    }


    fun saveWord() {
        viewModelScope.launch {
            Log.i("InputKataScreen","VM SaveWord called")

            val eId = System.currentTimeMillis()

            val eWTable = EnglishWordsTable(
                eId = eId,
                eWord = eWordM.value,
                definition = definitionM.value
            )

            repository.insertEnglishWord(eWTable)
            Log.i("InputKataScreen","VM ${iWordsListM.value}")
            iWordsListM.value.forEach { word ->

                val iId = System.currentTimeMillis()

                val iWTable = IndonesianWordsTable(
                    iId = iId,
                    iWord = word
                )
                Log.i("InputKataScreen","VM ${iWTable.iId} ${iWTable.iWord}")

                repository.insertIndonesianWord(iWTable)

                val crTable = CorrelatedWord(
                    eId = eId,
                    iId = iId
                )

                repository.insertCorrelatedWord(crTable)

                delay(1) // still ok if you want unique timestamps
            }
            resetValues()
        }
    }

    fun resetValues(){
        iWordsListM.value= emptyList()
        eWordM.value=""
        definitionM.value=""
    }
    fun setValues(eWord:String,defitinion:String,iWords:List<String>){
        eWordM.value=eWord
        definitionM.value=defitinion
        iWordsListM.value=iWords
    }

    val filteredWords = combine(wtModelList, _searchQuery) { words, query ->
        if (query.isBlank()) {
            words
        } else {
            words.filter { item ->
                item.english.eWord.contains(query, ignoreCase = true) ||
                        item.indonesianWords.any { it.iWord.contains(query, ignoreCase = true) }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}