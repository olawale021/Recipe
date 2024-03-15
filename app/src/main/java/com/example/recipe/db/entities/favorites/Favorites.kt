package com.example.recipe.db.entities.favorites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.users.User

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userID"],
            childColumns = ["userID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["recipeID"],
            childColumns = ["recipeID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userID"]),
        Index(value = ["recipeID"])
    ]
)
data class Favorite(
    @PrimaryKey(autoGenerate = true) val favoriteID: Int = 0,
    val userID: Int,
    val recipeID: Int
)