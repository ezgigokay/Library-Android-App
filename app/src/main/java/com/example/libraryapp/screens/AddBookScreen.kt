package com.example.libraryapp.screens
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    viewModel: BookViewModel,
    //bookEntity: BookEntity? = null,
    navController: NavHostController
) {


    var inputBookTitle by remember { mutableStateOf("") }
    var inputBookAuthor by remember { mutableStateOf("") }
    var inputPublishYear by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Book") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("MainScreen")}) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp)
        ) {
            Text(
                text = "Add New Book",
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 22.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = inputBookTitle,
                onValueChange = { inputBookTitle = it },
                label = { Text("Book Title") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                placeholder = { Text("Enter book title") }
            )

            OutlinedTextField(
                value = inputBookAuthor,
                onValueChange = { inputBookAuthor = it },
                label = { Text("Book Author") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                placeholder = { Text("Enter book author") }
            )

            OutlinedTextField(
                value = inputPublishYear,
                onValueChange = { enteredText ->
                    inputPublishYear = enteredText.filter { it.isDigit() }
                },
                label = { Text("Book Publish Year") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                placeholder = { Text("Enter book publish year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            var savedBook by remember { mutableStateOf<BookEntity?>(null) }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    val newBook = BookEntity(0, inputBookTitle, inputBookAuthor, inputPublishYear.toIntOrNull() ?: 0)
                    viewModel.addBook(newBook)
                    savedBook = newBook
                    Toast.makeText(context, "Book Added", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Book", color = MaterialTheme.colorScheme.onPrimary)
            }
            savedBook?.let { SelectedBook(book = it) }
        }
    }
}