package com.example.vocabularyproject.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vocabularyproject.database.tables.CorrelatedWord
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
}