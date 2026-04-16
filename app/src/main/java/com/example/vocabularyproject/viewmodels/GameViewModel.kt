package com.example.vocabularyproject.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyproject.database.VocabularyRepository
import com.example.vocabularyproject.database.models.IndonesianToEnglishModel
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.database.tables.CurrentItemModel
import com.example.vocabularyproject.util.BahasaPermaian
import com.example.vocabularyproject.util.OpsiPermaian
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isEmpty
import kotlin.text.toInt

@HiltViewModel
class GameViewModel@Inject constructor( private val repository: VocabularyRepository) : ViewModel() {

    private val _selectedOption= MutableStateFlow<String>("")
    val selectedOption : StateFlow<String> get() = _selectedOption

    private val _selectedLanguage= MutableStateFlow<String>("")
    val selectedLanguage : StateFlow<String> get() = _selectedLanguage

    private val _selectedBatch= MutableStateFlow<String>("")
    val selectedBatch : StateFlow<String> get() = _selectedBatch

    private val _batchQuantity= MutableStateFlow<Int>(20)
    val batchQuantity : StateFlow<Int> get() =_batchQuantity

    val wtModelList =
        repository.getWordTranslationFlowList()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val _newWtList= MutableStateFlow<List<WordTranslationModel>>(listOf())
    val newWtList : StateFlow<List<WordTranslationModel>> get() = _newWtList

    val _inaToEngList= MutableStateFlow<List<IndonesianToEnglishModel>>(listOf())
    val inaToEngList : StateFlow<List<IndonesianToEnglishModel>> get() = _inaToEngList

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
    val currentItemOld: StateFlow<WordTranslationModel?> = combine(
        newWtList,
        cardIndexM
    ) { list, index ->
        list.getOrNull(index)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
    val currentItem: StateFlow<CurrentItemModel?> = combine(
        selectedLanguage,
        _newWtList,
        _inaToEngList,
        cardIndexM
    ) { language, wtList, inaList, index ->
        when (language) {
            BahasaPermaian.opt1EnglishToIndonesian -> {
                val item = wtList.getOrNull(index) ?: return@combine null
                CurrentItemModel(
                    questionWord = item.english.eWord,
                    answerWord = item.indonesianWords.map{ it.iWord } ?:listOf(),
                    definition = item.english.definition
                )
            }
            else -> {
                val item = inaList.getOrNull(index) ?: return@combine null
                CurrentItemModel(
                    questionWord = item.indonesian.iWord,
                    answerWord = item.englishWords.map{ it.eWord } ?:listOf(),
                    definition = item.englishWords.firstOrNull()?.definition ?: ""
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
    val answerText: StateFlow<String> = currentItem.map { item ->
        item?.answerWord?.joinToString(", ") { it } ?: ""
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
        flow=if (_selectedLanguage.value== BahasaPermaian.opt1EnglishToIndonesian) repository.getEnglishWordCount() else repository.getIndonesianWordCount(),
        flow2=_batchQuantity
    ) { count, batch ->
        if (batch <= 0) emptyList()
        else {
            listOf("Acak") + (0 until count step batch).map { start ->
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

    fun onBahasaMenuChange(newValue :String){
        _selectedLanguage.value=newValue
    }
    fun onBatchListSelected(newValue: String){
        _selectedBatch.value=newValue
    }
    fun setBatchQuantity(newValue: String){
        _batchQuantity.value = if (newValue.isEmpty()) 0 else newValue.toInt() //
    }
    fun checkAnswer(){
        currentItem.value?.answerWord?.forEach {
            if (answer.value.trim().uppercase()==it.uppercase()){
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

    fun resetMutable(){
        cardIndexM.value=0
        _newWtList.value=emptyList()
        _inaToEngList.value=emptyList()
        isRevealedM.value=false
        isAnswerCorrectM.value=false
    }
    fun onGameStart(){
        resetMutable()
        if (_selectedLanguage.value== BahasaPermaian.opt1EnglishToIndonesian) {
            filterEnglishWords()
        }else {
            filterIndonesianWords()
        }
    }
    fun filterIndonesianWords(){
        viewModelScope.launch {

            when(selectedOption.value){
                OpsiPermaian.opt1semuakata->{
                    _inaToEngList.value=repository.getInaToEngTransationList().shuffled()
                }
                OpsiPermaian.opt3batchkata->{
                    val batch = _selectedBatch.value // e.g. "21 to 30"
                    if (batch=="Acak"){
                        _inaToEngList.value=repository.getInaToEngTransationList().shuffled().take(_batchQuantity.value)
                    }else{
                        val parts = batch.split("-")
                        val start = parts[0].trim().toIntOrNull()
                        val end = parts[1].trim().toIntOrNull()
                        if (start!=null && end!=null) {
                            val listRange=repository.getInaWordsByRange(start,end)
                            _inaToEngList.value = listRange.shuffled()
                        }
                    }
                }
                else->{}
            }
        }

    }
    fun filterEnglishWords(){
        viewModelScope.launch {
            when(selectedOption.value){
                OpsiPermaian.opt1semuakata->{
                    _newWtList.value=repository.getWordTranslationList().shuffled()
                }
                OpsiPermaian.opt3batchkata->{
                    val batch = _selectedBatch.value // e.g. "21 to 30"
                    if (batch=="Acak"){
                        _newWtList.value=repository.getWordTranslationList().shuffled().take(_batchQuantity.value)
                    }else {
                        val parts = batch.split("-")
                        val start = parts[0].trim().toIntOrNull()
                        val end = parts[1].trim().toIntOrNull()
                        if (start!=null && end!=null) {
                            val listRange=repository.getWordsByRange(start, end)
                            _newWtList.value = listRange.shuffled()
                        }
                    }
                }
                else->{}
            }
        }
    }
    fun resetIndex(){

    }
}