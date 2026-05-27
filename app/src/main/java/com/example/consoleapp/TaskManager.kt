package com.example.consoleapp
import java.io.File

//Defining task data class
data class Task(
    val Id: Int,
    var title: String,
    var taskStatus: TaskStatus = TaskStatus.TODO
)

//Defining status options of tasks
enum class TaskStatus {
    TODO,
    DONE
}

//Defining user interaction options
enum class SelectOptions(val option: String) {
    ADD_TASK("1"),
    COMPLETE_TASK("2"),
    LIST_ALL_COMPLETED_TASKS("3"),
    LIST_ALL_TASKS("4"),
    SEARCH_TASK("5"),
    DELETE_TASK("6"),
    SAVE_TASKS("7"),
    EXIT("8")
}

fun printWelcome() {
    println("\n========================")
    println("   KOTLIN TASK MANAGER  ")
    println("========================")
}

//Defining main source of truth
val taskList = mutableListOf<Task>()

var isRunning = true
var nextIdForTaskList = 1

fun main() {

    //Starting the loop for execution
    while (isRunning) {
        //Only show header
        if (taskList.isEmpty()) {
            printWelcome()
        }

        println("\nOptions: (1) Add  (2) Complete  (3) List All Completed Tasks  (4) List All the Tasks  (5) Search Task By Name  (6) Delete  (7) Save All Tasks  (8) Exit")
        print("Choose an option: ")

        when (readLine()) {
            //Adding task
            SelectOptions.ADD_TASK.option -> {
                addTask()
            }
            //Completing selected task by id
            SelectOptions.COMPLETE_TASK.option -> {
                completingSelectedTask()
            }
            //Listing all completed tasks
            SelectOptions.LIST_ALL_COMPLETED_TASKS.option -> {
                println("Listing all finished tasks")
                filterAndPrintCompletedTasks()
            }
            //Listing all tasks with their statuses
            SelectOptions.LIST_ALL_TASKS.option -> {
                println("Listing all the tasks")
                printTasks()
            }
            //Searching task by name
            SelectOptions.SEARCH_TASK.option -> {
                searchTask()
            }
            //Deleting task by Id
            SelectOptions.DELETE_TASK.option -> {
                deleteTask()
            }
            //Save all tasks
            SelectOptions.SAVE_TASKS.option -> {
                saveFile()
            }
            //Exiting
            SelectOptions.EXIT.option -> {
                println("Exiting..")
                isRunning = false
            }
        }
    }
}
//Handles user input for new tasks and adds them to the global list
private fun addTask() {
    print("Enter title: ")
    val title = readLine() ?: ""
    taskList.add(Task(getNewId(), title))
    println("Task added")
    printTasks()
}
//Mark selected task as done
private fun completingSelectedTask() {
    print("Choose task by ID to complete: ")
    val idInput = readLine()?.toIntOrNull()
    val task = taskList.find { it.Id == idInput }
    if (task != null) {
        task.taskStatus = TaskStatus.DONE
        println("${task.Id}.[✓] ${task.title} marked as complete.")
    } else {
        println("Error: Task with ID '$idInput' not found.")
    }
}

//Generates a new unique ID and increments the counter for the next task
private fun getNewId(): Int {
    return nextIdForTaskList++
}

//Prints a list of tasks with [ ] or [✓] markers
private fun printTasks(specificTask: List<Task>? = taskList) {
    if (specificTask != null) {
        for (task in specificTask) {
            val check = if (task.taskStatus == TaskStatus.DONE) "✓" else " "
            println("${task.Id}.[$check] ${task.title}")
        }
    }
}
//Filters the taskList based on a substring (case-insensitive)
private fun searchTask() {
    print("Enter title: ")
    val searchedInput = readLine().toString()
    taskList.filter { it.title.contains(searchedInput, ignoreCase = true) }.let {
        if (it.isNotEmpty()) {
            printTasks(it)
        } else (println("There is no task with searched title.."))
    }
}
//Specifically filters for completed tasks and prints them
private fun filterAndPrintCompletedTasks() {
    taskList.filter { it.taskStatus == TaskStatus.DONE }
        .let { completedTasks ->
            if (completedTasks.isEmpty()) {
                println("There is no completed task..")
            } else {
                for (task in completedTasks) {
                    println("${task.Id}.[✓] ${task.title}")
                }
            }
        }
}
//Safely removes a task by its ID
private fun deleteTask() {
    print("Pls enter task Id to delete: ")
    val enteredId = readln().toIntOrNull()
    val removed = taskList.removeIf { it.Id == enteredId }
    if (removed) {
        println("$enteredId. task deleted successfully")
        printTasks()
    } else {
        println("Task with ID $enteredId not found.")
    }
}

private fun saveFile() {
    val file = File("task_data.txt")
    val content = StringBuilder()

    for (task in taskList) {
        content.appendLine("${task.Id}. ${task.title} | ${task.taskStatus}")
    }
    file.writeText(content.toString())
    println("${taskList.size} number of tasks saved to ${file.name}")
}