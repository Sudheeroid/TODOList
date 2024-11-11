package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun addTodo(text: String) {
        viewModelScope.launch {
            try {
                // Simulate 3 second loading
                delay(3000)
                    if (text == "Error") {
                        throw Exception("Failed to add TODO")
                    }
                repository.addTodo(text)
                    _uiEvent.emit(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun deleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            try {
                repository.deleteTodo(todo)
                _uiEvent.emit(UiEvent.Success) // Emit success event
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.Error(e.message ?: "Unknown error"))
            }
        }
    }
}

sealed class UiEvent {
    object Success : UiEvent()
    data class Error(val message: String) : UiEvent()
}