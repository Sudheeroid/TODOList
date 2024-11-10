package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        observeTodos()
    }

    private fun observeTodos() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        repository.getAllTodos()
                    } else {
                        repository.searchTodos(query)
                    }
                }
                .catch { e ->
                    _state.update { it.copy(error = e.message) }
                }
                .collect { todos ->
                    _state.update {
                        it.copy(
                            todos = todos,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

data class MainScreenState(
    val todos: List<TodoItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)