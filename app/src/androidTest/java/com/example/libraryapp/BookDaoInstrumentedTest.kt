package com.example.libraryapp

import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.libraryapp.repository.SortType
import com.example.libraryapp.room.BookDAO
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class BookDaoInstrumentedTest {
    private lateinit var db: BooksDB
    private lateinit var dao: BookDAO

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BooksDB::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = db.bookDAO()
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun intertAndReadBack() = runTest {
        dao.addBook(BookEntity(0, "Dune", "Herbert", 1965))
        val all = dao.getAllBooks(SimpleSQLiteQuery("SELECT * FROM BookEntity")).first()
        assertEquals(1, all.size)
        assertEquals("Dune", all[0].title)
    }

    @Test
    fun deleteBook()= runTest {
        val newBook = BookEntity(1, "Dune", "Herbert", 1965)
        dao.addBook(newBook)
        dao.deleteBook(newBook)
        val all = dao.getAllBooks(SimpleSQLiteQuery("SELECT * FROM BookEntity")).first()
        assertEquals(0, all.size)
    }

    @Test
    fun updateBook() = runTest {
        val newBook = BookEntity(1, "Dune", "Herbert", 1965)
        dao.addBook(newBook)
        val updatedBook = newBook.copy(title = "Dune Messiah")
        dao.updateBook(updatedBook)
        val all = dao.getAllBooks(SimpleSQLiteQuery("SELECT * FROM BookEntity")).first()
        assertEquals(1, all.size)
        assertEquals("Dune Messiah", all[0].title)
    }


    @Test
    fun getAllBooks() = runTest {
        val all = dao.getAllBooks(SimpleSQLiteQuery("SELECT * FROM BookEntity")).first()
        assertEquals(0, all.size)
    }

    @Test
    fun getAllBooksOrderByPublicationYear() = runTest {
        dao.addBook(BookEntity(0, "1984", "Orwell", 1949))
        dao.addBook(BookEntity(0, "Dune", "Herbert", 1965))
        dao.addBook(BookEntity(0, "The Hobbit", "Tolkien", 1937))
        val sql = "SELECT * FROM BookEntity ORDER BY publicationYear DESC"
        val sorted = dao.getAllBooks(SimpleSQLiteQuery(sql)).first()
        assertEquals(listOf(1965, 1949, 1937), sorted.map { it.publicationYear })
    }


}