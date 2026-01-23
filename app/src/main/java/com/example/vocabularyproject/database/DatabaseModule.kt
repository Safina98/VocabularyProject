package com.example.vocabularyproject.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ):VocabularyDatabase =Room.databaseBuilder(
        context,
        VocabularyDatabase::class.java,
        "vocabulary.db"
    ).fallbackToDestructiveMigration(true)
        .build()
    @Provides
    fun provideEnglishWordsDao(db: VocabularyDatabase)=db.englishWordsDao()
    @Provides
    fun indonesianWordsDao(db: VocabularyDatabase)=db.indonesianWordsDao()
    @Provides
    fun correlatedWordsDao(db: VocabularyDatabase)=db.correlatedWordsDao()


}