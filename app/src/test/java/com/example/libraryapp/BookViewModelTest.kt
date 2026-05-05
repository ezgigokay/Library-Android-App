package com.example.libraryapp

import com.example.libraryapp.repository.BookRepository
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.repository.SortType
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.viewmodel.BookViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.plus
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private class FakeRepository : BookRepository {

        val books = MutableStateFlow(
            listOf(
                BookEntity(1, "Dune", "Herbert", 1965),
                BookEntity(2, "1984", "Orwell", 1949),
            )
        )
        var lastAdded: BookEntity? = null

        override fun getAllBooks(sortType: SortType) = books

        override suspend fun addBook(bookEntity: BookEntity) {
            lastAdded = bookEntity
            books.value = books.value + bookEntity
        }

        override suspend fun updateBook(bookEntity: BookEntity) {
            books.value = books.value.map { if (it.id == bookEntity.id) bookEntity else it }
        }

        override suspend fun deleteBook(bookEntity: BookEntity) {
            books.value = books.value - bookEntity
        }

    }
    private lateinit var fakeRepo: FakeRepository
    private lateinit var viewModel: BookViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        fakeRepo = FakeRepository()
        viewModel = BookViewModel(fakeRepo)
    }

    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `books are loaded from repository`() = runTest(dispatcher) {
        val result = viewModel.books.first()
        assertEquals(2, result.size)
        assertEquals("Dune", result[0].title)
    }

    @Test
    fun `books added to repository are emitted` () = runTest(dispatcher) {
        val newBook = BookEntity(3, "The Hobbit", "Tolkien", 1937)
        viewModel.addBook(newBook)
        advanceUntilIdle()
        val emitted = viewModel.books.first()
        assertEquals(3, emitted.size)
        assertEquals(newBook, emitted.last())
    }

    @Test
    fun `books deleted from repository are emitted` () = runTest(dispatcher) {
        val bookToDelete = fakeRepo.books.value.first()
        viewModel.deleteBook(bookToDelete)
        advanceUntilIdle()
        val emitted = viewModel.books.first()
        assertEquals(1, emitted.size)
        assert(!emitted.contains(bookToDelete))
    }

    @Test
    fun `books updated in repository are emitted` () = runTest(dispatcher) {
        val updatedBook = BookEntity(1, "Test Book", "Herbert", 1965)
        viewModel.updateBook(updatedBook)
        advanceUntilIdle()
        val emitted = viewModel.books.first()
        assertEquals(2, emitted.size)
        assert(emitted.contains(updatedBook))
    }
}