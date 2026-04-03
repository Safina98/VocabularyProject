package com.example.vocabularyproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyproject.database.VocabularyRepository
import com.example.vocabularyproject.database.models.WordTranslationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.text.isEmpty
import kotlin.text.toInt

@HiltViewModel
class GameViewModel@Inject constructor( private val repository: VocabularyRepository) : ViewModel() {

    private val _selectedOption= MutableStateFlow<String>("")
    val selectedOption : StateFlow<String> get() = _selectedOption

    private val _selectedBatch= MutableStateFlow<String>("")
    val selectedBatch : StateFlow<String> get() = _selectedBatch

    private val _batchQuantity= MutableStateFlow<Int>(20)
    val batchQuantity : StateFlow<Int> get() =_batchQuantity

    val wtModelList =
        repository.getWordTranslationList()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val randomizedWtList = wtModelList.map { list ->
        list.shuffled()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    private val cardIndexM = MutableStateFlow(0)
    val cardIndex: StateFlow<Int> = cardIndexM.asStateFlow()

    private val isRevealedM = MutableStateFlow(false)
    val isRevealed: StateFlow<Boolean> = isRevealedM.asStateFlow()

    // 2. The Current Item (derived from the list and the index)
    val currentItem: StateFlow<WordTranslationModel?> = combine(
        randomizedWtList,
        cardIndexM
    ) { list, index ->
        list.getOrNull(index)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
    val answerText: StateFlow<String> = currentItem.map { item ->
        item?.indonesianWords?.joinToString(", ") { it.iWord } ?: ""
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )
    private val isAnswerCorrectM=MutableStateFlow(false)
    val isAnswerCorrect: StateFlow<Boolean> = isAnswerCorrectM.asStateFlow()
    val answer = MutableStateFlow("")

    val currentId = MutableStateFlow<Long?>(null)



    val batchList: StateFlow<List<String>> = combine(
        repository.getWordCount(),
        _batchQuantity
    ) { count, batch ->
        if (batch <= 0) emptyList()
        else {
            (0 until count step batch).map { start ->
                val end = minOf(start + batch, count)
                "${start + 1}-${end}"
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun onOptionMenuChange(newValue :String){
        _selectedOption.value=newValue
    }
    fun onBatchListSelected(newValue: String){
        _selectedBatch.value=newValue
    }


    fun setBatchQuantity(newValue: String){
        _batchQuantity.value = if (newValue.isEmpty()) 0 else newValue.toInt() //
    }

    fun checkAnswer(){
        currentItem.value?.indonesianWords?.forEach {
            if (answer.value.trim().uppercase()==it.iWord.uppercase()){
                isAnswerCorrectM.value=true
            }

        }
    }
    fun setCurrentId(eId: Long) {
        currentId.value = eId
    }

    fun toggleReveal() {
        isRevealedM.value = true
    }
    fun nextCard() {
        // You can add logic here to stop at the end or loop
        cardIndexM.value++
        isRevealedM.value=false
        answer.value=""
        isAnswerCorrectM.value=false
    }

}