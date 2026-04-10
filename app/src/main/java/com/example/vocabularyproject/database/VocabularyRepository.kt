package com.example.vocabularyproject.database

import android.util.Log
import androidx.paging.PagingSource
import com.example.vocabularyproject.database.daos.CorrelatedWordsDao
import com.example.vocabularyproject.database.daos.EnglishWordsDao
import com.example.vocabularyproject.database.daos.IndonesianWordsDao
import com.example.vocabularyproject.database.models.IndonesianToEnglishModel
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VocabularyRepository @Inject constructor(
    private val englishWordsDao: EnglishWordsDao,
    private val indonesianWordsDao: IndonesianWordsDao,
    private val correlatedWordsDao: CorrelatedWordsDao
    ){

    fun getEnglishWordCount(): Flow<Int> = englishWordsDao.getCount()

    fun getIndonesianWordCount(): Flow<Int> = indonesianWordsDao.getCount()


    suspend fun saveFullWordEntry(eWord: String, definition: String, iWords: List<String>) {
        withContext(Dispatchers.IO) {
            correlatedWordsDao.saveFullWordEntry(eWord, definition, iWords)
        }
    }

    suspend fun insertEnglishWord(englishWord: EnglishWordsTable) {
        withContext(Dispatchers.IO){
            englishWordsDao.insert(englishWord)
        }
    }

    suspend fun deleteEnglishWord(id:Long){
        withContext(Dispatchers.IO){
            englishWordsDao.delete(id)
        }
    }
    suspend fun updateEWordById(id: Long, newWord: String) {
        withContext(Dispatchers.IO){
            englishWordsDao.updateEWordById(id, newWord)
        }
    }
    suspend fun updateDefinitionById(id: Long, definition: String) {
        withContext(Dispatchers.IO){
            englishWordsDao.updateDefinitionById(id, definition)
        }
    }

    fun getAllEnglishWords(): Flow<List<EnglishWordsTable>> =
        englishWordsDao.getEnglishWords()
    fun getWordTranslationFlowList(): Flow<List<WordTranslationModel>> =
        englishWordsDao.getWordTranslationFlowList()

    fun getWordTranslationPaged(): PagingSource<Int, WordTranslationModel> =
        englishWordsDao.getWordTranslationPaged()

    fun searchWordsPaged(query: String): PagingSource<Int, WordTranslationModel> =
        englishWordsDao.searchWordsPaged(query)




    suspend fun getWordTranslationList(): List<WordTranslationModel> {
        return withContext(Dispatchers.IO){
            englishWordsDao.getWordTranslationList()
        }
    }
    suspend fun getInaToEngTransationList(): List<IndonesianToEnglishModel> {
        return withContext(Dispatchers.IO){
            indonesianWordsDao.geInatoEngTranslationList()
        }
    }

    suspend fun getWordsByRange(start: Int, end: Int): List<WordTranslationModel> {
        return withContext(Dispatchers.IO){
            val offset = start - 1
            val limit = end - start + 1
             englishWordsDao.getWordsByRange(limit, offset)
        }
    }
    suspend fun getInaWordsByRange(start: Int, end: Int): List<IndonesianToEnglishModel> {
        return withContext(Dispatchers.IO){
            val offset = start - 1
            val limit = end - start + 1
           indonesianWordsDao.getIndonesianWordsByRange(limit,offset)
        }
    }


    //////////////////////////////////////////////////////////////////////////////////
    suspend fun insertIndonesianWord(indonesianWord: IndonesianWordsTable) {
        withContext(Dispatchers.IO){
            indonesianWordsDao.insert(indonesianWord)
        }
    }
    suspend fun deleteIndonesianWord(id:Long){
        withContext(Dispatchers.IO) {
            indonesianWordsDao.delete(id)
        }
    }

    suspend fun updateIWord(iId:Long,iWord:String) {
        withContext(Dispatchers.IO){
            indonesianWordsDao.updateIWordById(iId,iWord)
        }
    }
    fun getAllIndonesianWords():Flow<List<IndonesianWordsTable>> =
        indonesianWordsDao.selectIndonesianWords()

    fun getCorelatedIndonesianWords(eId:Long):Flow<List<IndonesianWordsTable>> =
        indonesianWordsDao.selectCorelatedIndonesanWords(eId)

    ////////////////////////////////////////////////////////////////////////////////
    suspend fun insertCorrelatedWord(correlatedWord: CorrelatedWord){
        withContext(Dispatchers.IO){
            Log.i("InputKataScreen","Repo insert called")
            correlatedWordsDao.insert(correlatedWord)
        }
    }
    fun getAllCorrelatedId(): Flow<List<CorrelatedWord>> =
        correlatedWordsDao.selectAllCorrelatedIds()
}