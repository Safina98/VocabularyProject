package com.example.vocabularyproject.database.tables

data class CurrentItemModel(
    val questionWord:String,
    val answerWord: List<String>,
    val definition:String,
            )