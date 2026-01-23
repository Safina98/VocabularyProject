package com.example.vocabularyproject.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indonesian_words")
data class IndonesianWordsTable(
    @PrimaryKey
    var iId: Long = 0L,
    var iWord: String=""
)
