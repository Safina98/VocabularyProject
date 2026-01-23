package com.example.vocabularyproject.database.tables

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
data class CorrelatedWord(
    @PrimaryKey(autoGenerate = true)
    var cId: Long = 0L,
    var eId: Long=0L,
    var iId: Long=0L
)
