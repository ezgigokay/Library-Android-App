package com.example.consoleapp

import java.time.LocalDate

data class Book(
    val isbn: Int,
    val title: String,
    val author: String,
    var isAvailable: Boolean = true
)

data class Members(
    val Id: Int,
    val memberName: String,
    val borrowedBook: MutableList<Book> = mutableListOf()
)

data class LoanRecord(
    val transaction: String,
    val bookIsbn: Int,
    val memberId: Int,
    val dueDate: LocalDate?
)

val catalog = mutableMapOf<Int, Book>()
val members = mutableSetOf<Members>()
val transaction = mutableListOf<LoanRecord>()
var nextISBN = 5
var nextUserId = 1

open class SmartLibrary {
    open fun initializeLibrary() {
        catalog[1] = Book(1, "Dead Souls", "Gogol", false)
        catalog[2] = Book(2, "The Overcoat", "Gogol", true)
        catalog[3] = Book(3, "Miserables", "Viktor Hugo", true)
        catalog[4] = Book(4, "Metamorphosis", "Kafka", true)
        catalog[5] = Book(5, "The Brothers Karamazov", "Dostoevsky", true)
        print("Library initialized with ${catalog.size} books.")
    }
    fun start() {
        initializeLibrary()
        var running = true
        while (running) {
            println("\n--- SMART LIBRARY MAIN MENU ---")
            println("1. Book Management")
            println("2. User Management")
            println("3. History")
            println("4. Exit")
            print("Pls Enter selection:  ")

            when (readln()) {
                "1" -> catalogMenu()
                "2" -> userMenu()
                "3" -> transaction()
                "4" -> running = false
                else -> print("Pls select a valid option")
            }
        }
    }
    fun catalogMenu() {
        var inSubmenu = true
        while (inSubmenu) {
            println("\n--- BOOK MANAGEMENT ---")
            println("1. View All Books")
            println("2. Search For A Book")
            println("3. Add New Book")
            println("0. Back to Main Menu")
            print("Pls Enter selection:  ")

            when (readln()) {
                "1" -> viewAllBooks()
                "2" -> searchBook()
                "3" -> addANewBook()
                "0" -> inSubmenu = false
                else -> print("Pls select a valid option")
            }
        }
    }
    fun userMenu() {
        var insubMenu = true
        while (insubMenu) {
            println("\n--- USER MANAGEMENT ---")
            println("1. View All Users")
            println("2. Add New User")
            println("3. Borrow A Book")
            println("0. Back to Main Menu")
            print("Pls Enter selection:  ")

            when (readln()) {
                "1" -> displayAllUsers()
                "2" -> addNewMember()
                "3" -> borrowABook()
                "0" -> insubMenu = false
                else -> print("Pls select a valid option")
            }
        }
    }
    fun transaction() {
        var insubMenu = true
        while (insubMenu) {
            println("\n--- HISTORY ---")
            println("1. View All Transactions")
            println("2. View Currently Borrowed Books")
            println("0. Back to Main Menu")
            print("Pls Enter selection:  ")


            when (readln()) {
                "1" -> viewAllTransactions()
                "2" -> viewBorrowedBooks()
                "0" -> insubMenu = false
                else -> print("Pls select a valid option")
            }
        }
    }
    fun viewAllBooks() {
        for (book in catalog.values) {
            println("${book.isbn}. ${book.title} by ${book.author} -${getStatusText(book)}")
        }
        catalog.ifEmpty {
            println("There is no book currently")
        }
    }
    fun addANewBook() {
        print("Pls enter title of book: ")
        val title = readln().replaceFirstChar { it.uppercase() }
        print("Pls enter name of book: ")
        val author = readln().replaceFirstChar { it.uppercase() }
        val id = nextISBN++
        catalog[id] = Book(id, title, author)
        println("Success '$title' added to catalog")
        viewAllBooks()
    }
    fun addNewMember() {
        print("Pls enter user name: ")
        val memberName = readln().replaceFirstChar { it.uppercase() }
        val userId = nextUserId++
        val newMember = Members(userId, memberName)
        members.add(newMember)
        displayAllUsers()
    }
    fun borrowABook() {
        print("Pls enter user name: ")
        val userName = readln()
        print("Pls enter book title: ")
        val bookTitle = readln()
        val member = members.find { it.memberName == userName }
        val book = catalog.values.find { it.title.contains(bookTitle, ignoreCase = true) }
        if (member != null && book != null && book.isAvailable) {
            member.borrowedBook.add(book)
            book.isAvailable = false
            val timestamp = LocalDate.now()
            val dueDate = LocalDate.now().plusDays(2)
            val transactionId = "TX-${member.Id}.${book.isbn}.$timestamp"
            transaction.add(LoanRecord(transactionId, book.isbn, member.Id, dueDate))
            println("Success! ${book.title} added to ${member.memberName}'s list.")
        } else if (member != null && book != null) {
            print("${book.title} is not available")
        }
        else {
            println("Error: Could not find that member or book.")
        }
    }
    fun displayAllUsers() {
        members.ifEmpty {
            println("There is no member added yet")
        }
        for (member in members) {
            println("${member.Id}. ${member.memberName} -- borrowed books ${member.borrowedBook}")
        }
    }
    fun searchBook() {
        print("Pls enter book title: ")
        val item = readln()
        var found = false
        for (book in catalog.values) {
            if (book.title.contains(item, ignoreCase = true)) {
                print("${book.title} is in library. -${getStatusText(book)}")
                found = true
                break
            }
        }
        if (!found) {
            println("Book is not in library")
        }
    }
    fun getStatusText(book: Book): String {
        return if (book.isAvailable) "is available" else "is not available"
    }
    fun viewAllTransactions() {
        transaction.ifEmpty {
            println("The transaction history is currently empty")
        }
        for (item in transaction) {
            println("Book ISBN: ${item.bookIsbn} borrewed by UserID: ${item.memberId}, Date: ${item.dueDate}, transaction ID: ${item.transaction}")
        }
    }
    fun viewBorrowedBooks() {
        val unavailableBooks = catalog.values.filter { !it.isAvailable }
        unavailableBooks.ifEmpty {
            println("There is no borrowed book")
        }
        for (book in unavailableBooks) {
            val checkTransaction = transaction.find { book.isbn == it.bookIsbn }
            if (checkTransaction != null) {
                val borrower = members.find { it.Id == checkTransaction.memberId }
                val borrowerName = borrower?.memberName
                println("${borrower?.borrowedBook} is borrowed by $borrowerName user")
            }
        }
    }
}

fun main() {
    val myLibrary = SmartLibrary()
    myLibrary.start()
}