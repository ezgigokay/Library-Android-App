package com.example.libraryapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDAO {

    @Insert
    suspend fun addBook(bookEntity: BookEntity)

    @Update
    suspend fun updateBook(bookEntity: BookEntity)

    @Delete
    suspend fun deleteBook(bookEntity: BookEntity)

    @RawQuery(observedEntities = [BookEntity::class])
    fun getAllBooks(query: SupportSQLiteQuery): Flow<List<BookEntity>>

}