package com.example.vocabularyproject.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import kotlinx.coroutines.flow.Flow

@Dao
interface IndonesianWordsDao {

    @Insert
    fun insert(indonesianWord: IndonesianWordsTable)

    @Update
    fun update(indonesianWord: IndonesianWordsTable)

    @Query("DELETE FROM indonesian_words WHERE iId = :id")
    fun delete(id:Long)

    @Query("SELECT * FROM indonesian_words")
    fun selectIndonesianWords(): Flow<List<IndonesianWordsTable>>
}