package com.example.passwordapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val username: String,

    val password: String,

    val category: String,

    val isFavorite: Boolean = false
)