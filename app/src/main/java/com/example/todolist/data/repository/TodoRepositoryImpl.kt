package com.example.todolist.data.repository

import com.example.todolist.data.local.TodoDao
import com.example.todolist.data.remote.model.TodoItem
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor() : ITodoRepository {
    private val todos = mutableListOf<TodoItem>()

    override suspend fun addTodo(text: String) {
        val newTodo = TodoItem(id = System.currentTimeMillis(), text = text)
        todos.add(newTodo)
    }

    override suspend fun deleteTodo(todo: TodoItem) {
        todos.remove(todo)
    }
    fun getAllTodos(): List<TodoItem> {
        return todos.toList() // Return a copy of the list
    }
}