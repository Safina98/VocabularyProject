package com.example.vocabularyproject.database.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "indonesian_words",
    indices = [Index(value = ["iWord"], unique = true)] // This makes eWord unique
    )
data class IndonesianWordsTable(
    @PrimaryKey
    var iId: Long = 0L,
    var iWord: String=""
)
