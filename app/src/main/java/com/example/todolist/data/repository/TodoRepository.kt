package com.example.todolist.data.repository

import com.example.todolist.data.local.TodoDao
import com.example.todolist.data.model.TodoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getAllTodos(): Flow<List<TodoItem>> = todoDao.getAllTodos()

    fun searchTodos(query: String): Flow<List<TodoItem>> = todoDao.searchTodos(query)

    open suspend fun addTodo(text: String) {
        if (text == "Error") throw Exception("Failed to add TODO")
        todoDao.insertTodo(TodoItem(text = text))
    }

    suspend fun deleteTodo(todo: TodoItem) {
        todoDao.deleteTodo(todo)
    }
}