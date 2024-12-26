package com.example.todolist.data.repository

import com.example.todolist.data.remote.model.TodoItem

interface ITodoRepository {
    suspend fun addTodo(text: String)
    suspend fun deleteTodo(todo: TodoItem)
}