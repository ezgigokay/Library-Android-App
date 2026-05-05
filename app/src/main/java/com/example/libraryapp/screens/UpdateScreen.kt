package com.example.libraryapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScren(viewModel: BookViewModel, bookId: String?, navControler: androidx.navigation.NavHostController) {
    val books by viewModel.books.collectAsState(initial = emptyList())
    val selectedBookId = bookId?.toIntOrNull()
    val selectedBook = books.firstOrNull { it.id == selectedBookId }

    var newTitleBook by remember { mutableStateOf("") }
    var newAuthor by remember { mutableStateOf("") }
    var newPublishDate by remember { mutableStateOf("") }

    LaunchedEffect(selectedBook?.id) {
        if (selectedBook != null) {
            newTitleBook = selectedBook.title
            newAuthor = selectedBook.author
            newPublishDate = selectedBook.publicationYear.toString()
        }
    }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Update Book") },
                navigationIcon = {
                    IconButton(onClick = {navControler.navigate("MainScreen")}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = newTitleBook,
                onValueChange = { enteredText -> newTitleBook = enteredText },
                label = { Text(text = "Update Book Name") },
                placeholder = { Text(text = "Enter new book name") }
            )

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = newAuthor,
                onValueChange = { enteredText -> newAuthor = enteredText },
                label = { Text(text = "Update Book Author") },
                placeholder = { Text(text = "Enter new author") }
            )

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = newPublishDate,
                onValueChange = { enteredText ->
                    newPublishDate = enteredText.filter { it.isDigit() }
                },
                label = { Text(text = "Update Book Publish Date") },
                placeholder = { Text(text = "Enter new publish date") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (selectedBookId != null) {
                        val updatedBook = BookEntity(
                            selectedBookId,
                            newTitleBook,
                            newAuthor,
                            newPublishDate.toIntOrNull() ?: 0
                        )
                        viewModel.updateBook(book = updatedBook)
                        Toast.makeText(context, "Book Updated", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Update")
            }

            SelectedBook(book = selectedBook)
        }
    }
}

@Composable
fun SelectedBook(book: BookEntity?) {
    Text(
        text = "Book Details",
        fontSize = 28.sp,
        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
        color = MaterialTheme.colorScheme.primary
    )

    if (book == null) {
        Text(
            text = "Book not found",
            color = MaterialTheme.colorScheme.error
        )
        return
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .padding(top = 8.dp)
            .shadow(2.dp, shape = MaterialTheme.shapes.medium)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID: ${book.id}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
            Text(
                text = "Title: ${book.title}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Text(
                text = "Author: ${book.author}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Text(
                text = "Year: ${book.publicationYear}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }
    }
}