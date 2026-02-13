package com.example.vocabularyproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularyproject.database.VocabularyRepository
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val translationsM: StateFlow<List<IndonesianWordsTable>> = currentId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getCorelatedIndonesianWords(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
    fun checkAnswer(){
       currentItem.value?.indonesianWords?.forEach {
           if (answer.value.trim().uppercase()==it.iWord.uppercase()){
               isAnswerCorrectM.value=true
           }
       }
        if (isAnswerCorrectM.value){
            Log.i("PermainanScreen","Your answer is correct")
        }else{
            Log.i("PermainanScreen","Your answer is incorrect")
            Log.i("PermainanScreen","The answers are:")
            currentItem.value?.indonesianWords?.forEach {
                Log.i("PermainanScreen","${it.iWord}")
            }
            Log.i("PermainanScreen","Your Answer : ${answer.value}")
        }
    }
    fun addWord(word:String) {
        iWordsListM.value=iWordsListM.value+word.trim().uppercase()
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
            Log.i("DAFTARKATASCREEN","Parameter id in viewModel is: $iId")
            if (iId!=0L){
                repository.updateIWord(iId,iWord)
            }
            else{
                val iWord=IndonesianWordsTable(iId= System.currentTimeMillis(),iWord = iWord)
                Log.i("DAFTARKATASCREEN","New assigned id si: ${iWord.iId}")
                val cWord=CorrelatedWord(eId = currentId.value!!,iId = iWord.iId)
                repository.insertIndonesianWord(iWord)
                repository.insertCorrelatedWord(cWord)
            }
        }
    }

    fun deleteEWord(eId:Long){
        viewModelScope.launch {
            repository.deleteEnglishWord(eId)
        }
    }
    fun deleteIWord(iId:Long){
        viewModelScope.launch {
            repository.deleteIndonesianWord(iId)
        }
    }
    fun deleteIWords(iWords:List<IndonesianWordsTable>){
        viewModelScope.launch {
            deleteIndonesianWordList(iWords)
        }
    }
    private suspend fun deleteIndonesianWordList(iWords:List<IndonesianWordsTable>){
        withContext(Dispatchers.IO){
            iWords.forEach {
                repository.deleteIndonesianWord(it.iId)
            }
        }
    }
    fun generateDummyData() {
        viewModelScope.launch {
            repository.insertDummyData()
        }
    }

    fun saveWord() {
        viewModelScope.launch {
            repository.saveFullWordEntry(eWordM.value.trim().uppercase(),definitionM.value.trim().uppercase(),iWordsListM.value)

//            val eId = System.currentTimeMillis()
//            val eWTable = EnglishWordsTable(
//                eId = eId,
//                eWord = eWordM.value,
//                definition = definitionM.value
//            )
//            repository.insertEnglishWord(eWTable)
//            iWordsListM.value.forEach { word ->
//                val iId = System.currentTimeMillis()
//                val iWTable = IndonesianWordsTable(
//                    iId = iId,
//                    iWord = word
//                )
//                repository.insertIndonesianWord(iWTable)
//                val crTable = CorrelatedWord(
//                    eId = eId,
//                    iId = iId
//                )
//                repository.insertCorrelatedWord(crTable)
//                delay(1) // still ok if you want unique timestamps
//            }
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