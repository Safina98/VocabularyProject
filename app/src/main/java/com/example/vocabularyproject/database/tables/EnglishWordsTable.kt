package com.example.vocabularyproject.database.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "english_words",
    indices = [Index(value = ["eWord"], unique = true)] // This makes eWord unique
)
data class EnglishWordsTable(
    @PrimaryKey
    var eId: Long = 0L,
    var eWord: String = "",
    var definition: String = ""
)
