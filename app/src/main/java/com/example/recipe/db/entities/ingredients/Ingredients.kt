package com.example.recipe.db.entities.ingredients

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientID: Int = 0,
    val name: String,
    val description: String? // Nullable because the description is optional
)
