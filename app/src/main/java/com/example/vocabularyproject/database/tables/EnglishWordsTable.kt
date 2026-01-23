package com.example.vocabularyproject.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "english_words")
data class EnglishWordsTable(
    @PrimaryKey
    var eId: Long = 0L,

    var eWord: String="",

    var definition: String=""
)
