package com.example.vocabularyproject.database.tables

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "correlated_words",
    foreignKeys = [
        ForeignKey(
            entity = EnglishWordsTable::class,
            parentColumns = ["eId"],
            childColumns = ["eId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = IndonesianWordsTable::class,
            parentColumns = ["iId"],
            childColumns = ["iId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("eId"),
        Index("iId")
    ]
)
@Immutable
data class CorrelatedWord(
    @PrimaryKey(autoGenerate = true)
    val cId: Long = 0L,
    val eId: Long=0L,
    val iId: Long=0L
)
