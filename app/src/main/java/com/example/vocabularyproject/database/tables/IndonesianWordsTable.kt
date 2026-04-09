package com.example.vocabularyproject.database.tables

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "indonesian_words",
    indices = [Index(value = ["iWord"], unique = true)] // This makes eWord unique
    )
@Immutable
data class IndonesianWordsTable(
    @PrimaryKey
    val iId: Long = 0L,
    val iWord: String=""
)
