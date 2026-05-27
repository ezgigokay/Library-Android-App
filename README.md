# 📚 Library App – Android

A personal book tracking app built with modern Android development practices. Users can add, edit, delete, and sort books from a persistent local database.

---

## Features

- 📖 View all books in a scrollable list
- ➕ Add new books (title, author, publication year)
- ✏️ Edit existing book details
- 🗑️ Delete books with a confirmation dialog
- 🔃 Sort books by ID, title, author, or publication year

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| State Management | ViewModel + StateFlow |
| Database | Room (SQLite) |
| Dependency Injection | Hilt |
| Language | Kotlin |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 |

---

## Architecture

The app follows a clean layered architecture:

```
UI Layer       → MainActivity, AddBookScreen, UpdateScreen (Jetpack Compose)
ViewModel      → BookViewModel (business logic, state holder)
Repository     → Repository / BookRepository (data access abstraction)
Data Layer     → Room Database (BookDAO, BookEntity, BooksDB)
DI             → Hilt AppModule wires everything together
```

---

## Project Structure

```
app/src/main/java/com/example/libraryapp/
├── di/                  # Hilt dependency injection module
├── repository/          # Repository interface and implementation
├── room/                # Room DB, DAO, and Entity
├── screens/             # AddBookScreen and UpdateScreen composables
├── ui/theme/            # Material3 theme (colors, typography)
├── viewmodel/           # BookViewModel
├── LibraryApplication.kt
└── MainActivity.kt      # App entry point and main screen
```

---

## Tests

- **Unit tests** (`BookViewModelTest`) — tests ViewModel logic using a `FakeRepository`, no Android dependencies
- **Instrumented tests** (`BookDaoInstrumentedTest`) — tests Room DAO operations using an in-memory database on a real device/emulator

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/ezgigokay/Library-Android-App.git
   ```
2. Open in **Android Studio**
3. Run on an emulator or physical device (API 24+)

