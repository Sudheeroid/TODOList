package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem

interface ITodoRepository {
    suspend fun addTodo(text: String)
    suspend fun deleteTodo(todo: TodoItem)
}