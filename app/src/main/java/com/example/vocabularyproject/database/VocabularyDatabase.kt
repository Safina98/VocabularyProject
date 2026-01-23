package com.example.vocabularyproject.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vocabularyproject.database.daos.CorrelatedWordsDao
import com.example.vocabularyproject.database.daos.EnglishWordsDao
import com.example.vocabularyproject.database.daos.IndonesianWordsDao
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable

@Database(
    entities = [
        EnglishWordsTable::class,
        IndonesianWordsTable::class,
        CorrelatedWord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VocabularyDatabase : RoomDatabase() {

    abstract fun englishWordsDao(): EnglishWordsDao
    abstract fun indonesianWordsDao(): IndonesianWordsDao
    abstract fun correlatedWordsDao(): CorrelatedWordsDao
}
