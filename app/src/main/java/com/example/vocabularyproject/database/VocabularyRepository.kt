package com.example.vocabularyproject.database

import android.util.Log
import com.example.vocabularyproject.database.daos.CorrelatedWordsDao
import com.example.vocabularyproject.database.daos.EnglishWordsDao
import com.example.vocabularyproject.database.daos.IndonesianWordsDao
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

    suspend fun insertEnglishWord(englishWord: EnglishWordsTable) {
        withContext(Dispatchers.IO){
            englishWordsDao.insert(englishWord)
        }
    }

    suspend fun updateEnglishWord(englishWord: EnglishWordsTable) {
     withContext(Dispatchers.IO){
         englishWordsDao.update(englishWord)
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
    fun getWordTranslationList(): Flow<List<WordTranslationModel>> =
        englishWordsDao.getWordTranslationList()


    //////////////////////////////////////////////////////////////////////////////////
    suspend fun insertIndonesianWord(indonesianWord: IndonesianWordsTable) {
        withContext(Dispatchers.IO){
            Log.i("InputKataScreen","Repo insert called")
            indonesianWordsDao.insert(indonesianWord)
        }
    }
    suspend fun updateIWord(iId:Long,iWord:String) {
        withContext(Dispatchers.IO){
            indonesianWordsDao.updateIWordById(iId,iWord)
        }
    }
    fun getAllIndonesianWords():Flow<List<IndonesianWordsTable>> =
        indonesianWordsDao.selectIndonesianWords()

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