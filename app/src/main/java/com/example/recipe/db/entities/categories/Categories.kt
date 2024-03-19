package com.example.recipe.db.entities.categories

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryID: Int = 0,
    val name: String,
    val description: String? // Nullable to allow optional descriptions
)