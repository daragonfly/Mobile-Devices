package com.example.myapplication

data class TodoItem(
    val title: String,
    val description: String,
    val dueDate: String,
    val imageUri: String? = null
)
