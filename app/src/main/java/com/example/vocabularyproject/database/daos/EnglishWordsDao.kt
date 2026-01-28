package com.example.vocabularyproject.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import kotlinx.coroutines.flow.Flow

@Dao
interface EnglishWordsDao {

    @Insert
    fun insert(englishWord: EnglishWordsTable)

    @Update
    fun update(englishWord: EnglishWordsTable)

    @Query("DELETE FROM english_words WHERE eId = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM english_words")
    fun getEnglishWords(): Flow<List<EnglishWordsTable>>

    @Transaction
    @Query("SELECT * FROM english_words")
    fun getWordTranslationList(): Flow<List<WordTranslationModel>>


}