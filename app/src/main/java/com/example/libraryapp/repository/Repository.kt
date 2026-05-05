package com.example.libraryapp.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import kotlinx.coroutines.flow.Flow

class Repository(val booksDB: BooksDB) : BookRepository {

    override suspend fun addBook(book: BookEntity) {
        booksDB.bookDAO().addBook(book)
    }

    override suspend fun updateBook(book: BookEntity) {
        booksDB.bookDAO().updateBook(book)
    }

    override suspend fun deleteBook(book: BookEntity) {
        booksDB.bookDAO().deleteBook(book)
    }

    override fun getAllBooks(sortType: SortType): Flow<List<BookEntity>> {
        val orderBy = when (sortType) {
            SortType.ID -> "id ASC"
            SortType.PUBLICATION_YEAR -> "publicationYear DESC"
            SortType.TITLE -> "title COLLATE NOCASE ASC"
            SortType.AUTHOR -> "author COLLATE NOCASE ASC"
        }
        val sql = "SELECT * FROM BookEntity ORDER BY $orderBy"
        return booksDB.bookDAO().getAllBooks(SimpleSQLiteQuery(sql))
    }
}

enum class SortType {
    ID, PUBLICATION_YEAR, TITLE, AUTHOR
}