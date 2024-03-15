package com.example.recipe.db.entities.recipes

import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.recipe.db.entities.users.User
import java.util.*

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userID"],
            childColumns = ["userID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userID"])]
)
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipeID: Int = 0,
    val userID: Int,
    val title: String,
    val description: String?,
    val servingSize: Int,
    val createDate: Date,
    val updateDate: Date,
    val image: String? // Assuming URL or file path as a string
)