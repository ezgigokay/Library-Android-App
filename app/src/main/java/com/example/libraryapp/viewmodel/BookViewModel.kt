package com.example.libraryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.repository.BookRepository
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.repository.SortType
import com.example.libraryapp.room.BookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookViewModel @Inject constructor(val repository: BookRepository) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.ID)
    val books = _sortType.flatMapLatest { repository.getAllBooks(it) }
    fun setSort(sortType: SortType) { _sortType.value = sortType }

    fun addBook(book: BookEntity) {
        viewModelScope.launch {
            repository.addBook(book)
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    fun updateBook(book: BookEntity) {
        viewModelScope.launch {
            repository.updateBook(book)
        }
    }
}