package com.example.todolist.ui.state

data class DetailsScreenState(
    val text: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)