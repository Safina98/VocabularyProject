package com.example.vocabularyproject.database.tables

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "english_words",
    indices = [Index(value = ["eWord"], unique = true)] // This makes eWord unique
)
@Immutable
data class EnglishWordsTable(
    @PrimaryKey
    val eId: Long = 0L,
    val eWord: String = "",
    val definition: String = ""
)
