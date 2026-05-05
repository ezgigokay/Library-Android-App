package com.example.libraryapp.di

import android.content.Context
import com.example.libraryapp.repository.BookRepository
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BooksDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBooksDB(@ApplicationContext context: Context): BooksDB =
        BooksDB.getInstance(context)

    @Provides
    @Singleton
    fun provideRepository(db: BooksDB): BookRepository = Repository(db)
}
