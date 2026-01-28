package com.example.vocabularyproject.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.vocabularyproject.database.tables.CorrelatedWord
import com.example.vocabularyproject.database.tables.EnglishWordsTable
import com.example.vocabularyproject.database.tables.IndonesianWordsTable

data class WordTranslationModel(
    @Embedded
    val english: EnglishWordsTable,

    @Relation(
        parentColumn = "eId",
        entityColumn = "iId",
        associateBy = Junction(
            value = CorrelatedWord::class,
            parentColumn = "eId",
            entityColumn = "iId"
        )
    )
    val indonesianWords: List<IndonesianWordsTable>
)
