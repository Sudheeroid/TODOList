package com.example.todolist.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.model.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "todos_db"
    }
}