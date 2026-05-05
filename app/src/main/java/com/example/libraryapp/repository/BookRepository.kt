package com.example.libraryapp.repository

import com.example.libraryapp.room.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun addBook(bookEntity: BookEntity)
    suspend fun updateBook(bookEntity: BookEntity)
    suspend fun deleteBook(bookEntity: BookEntity)
    fun getAllBooks(sortType: SortType): Flow<List<BookEntity>>
}