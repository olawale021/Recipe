package com.example.recipe.db.entities.users

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userID: Int = 0,
    val username: String,
    val email: String,
    val password: String,

)