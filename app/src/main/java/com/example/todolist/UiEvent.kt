package com.example.todolist

sealed class UiEvent {
    object Success : UiEvent()
    data class Error(val message: String) : UiEvent()
}
