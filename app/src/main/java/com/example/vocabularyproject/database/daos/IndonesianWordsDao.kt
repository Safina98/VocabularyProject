package com.example.vocabularyproject.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vocabularyproject.database.tables.IndonesianWordsTable
import kotlinx.coroutines.flow.Flow

@Dao
interface IndonesianWordsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(indonesianWord: IndonesianWordsTable)

    @Update
    fun update(indonesianWord: IndonesianWordsTable)

    @Query("""
        UPDATE indonesian_words
        SET iWord = :newWord 
        WHERE iId = :id
    """)
    suspend fun updateIWordById(
        id: Long,
        newWord: String
    )
    @Query("DELETE FROM indonesian_words WHERE iId = :id")
    fun delete(id:Long)

    @Query("SELECT * FROM indonesian_words")
    fun selectIndonesianWords(): Flow<List<IndonesianWordsTable>>

    @Query("""
        SELECT i.* FROM indonesian_words i
        INNER JOIN correlated_words c ON i.iId = c.iId
        WHERE c.eId =:eId
    """)
    fun selectCorelatedIndonesanWords(eId:Long):Flow<List<IndonesianWordsTable>>

}