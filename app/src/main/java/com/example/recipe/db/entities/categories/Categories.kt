package com.example.recipe.db.entities.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryID: Int = 0,
    val name: String,
    val description: String? // Nullable to allow optional descriptions
)