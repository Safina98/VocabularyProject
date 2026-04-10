package com.example.vocabularyproject.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable

data class IndonesianToEnglishModel (
    @Embedded
    val indonesian: IndonesianWordsTable,
    @Relation(
        parentColumn = "iId",
        entityColumn = "eId",
        associateBy = Junction(
            value = CorrelatedWord::class,
            parentColumn = "iId",
            entityColumn = "eId"
        )
    )
    val englishWords: List<EnglishWordsTable>
)