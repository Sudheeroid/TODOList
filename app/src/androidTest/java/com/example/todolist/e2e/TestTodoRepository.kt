package com.example.todolist.e2e

import com.example.todolist.data.local.TodoDao
import com.example.todolist.data.repository.TodoRepository
import javax.inject.Inject

class TestTodoRepository  @Inject constructor(todoDao: TodoDao) : TodoRepository(todoDao) {
    private val todos = mutableListOf<String>()

    override suspend fun addTodo(text: String) {
        if (text == "Error") throw Exception("Failed to add TODO")
        todos.add(text)
    }

    fun clear() {
        todos.clear()
    }
}