package com.example.vocabularyproject.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CorrelatedWordsDao {

    @Insert
    fun insert(correlatedWord: CorrelatedWord)

    @Update
    fun update(correlatedWord: CorrelatedWord)

    @Query("DELETE FROM correlated_words WHERE cId = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM correlated_words")
    fun selectAllCorrelatedIds(): Flow<List<CorrelatedWord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEnglishWord(word: EnglishWordsTable): Long

    @Query("SELECT eId FROM english_words WHERE eWord = :eWord")
    suspend fun getEnglishWordId(eWord: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIndonesianWord(word: IndonesianWordsTable): Long

    @Query("SELECT iId FROM indonesian_words WHERE iWord = :iWord")
    suspend fun getIndonesianWordId(iWord: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCorrelatedWord(crossRef: CorrelatedWord)

    @Query("SELECT cId FROM correlated_words WHERE eId = :eId AND iId = :iId")
    suspend fun checkIfCorrelationExist(eId:Long, iId:Long):Long?

    // --- The Professional Transactional Method ---
    @Transaction
    open suspend fun saveFullWordEntry(eWord: String, definition: String, iWords: List<String>) {
        val cleanEWord = eWord.trim().uppercase()

        // 1. Get or Create English Word
        var finalEId = getEnglishWordId(cleanEWord)
        if (finalEId == null) {
            finalEId = System.currentTimeMillis()
            insertEnglishWord(EnglishWordsTable(finalEId, cleanEWord, definition.trim()))
        }
        // 2. Process Indonesian words
        iWords.forEach { word ->
            var finalIId = getIndonesianWordId(word)
            if (finalIId == null) {
                // Your manual ID logic
                kotlinx.coroutines.delay(1)
                finalIId = System.currentTimeMillis()
                insertIndonesianWord(IndonesianWordsTable(finalIId, word))
            }
            // 3. Link them
            val isCorrelationExixt = checkIfCorrelationExist(finalEId, finalIId)
            if (isCorrelationExixt==null){
                insertCorrelatedWord(CorrelatedWord(eId = finalEId, iId = finalIId))
            }
        }
    }
}