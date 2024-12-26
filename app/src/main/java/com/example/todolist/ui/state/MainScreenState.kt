package com.example.todolist.ui.state


import com.example.todolist.data.remote.model.TodoItem

data class MainScreenState(
    val todos: List<TodoItem> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

