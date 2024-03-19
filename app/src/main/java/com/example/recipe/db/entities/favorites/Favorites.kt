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
            parentColumns = ["id"], // Updated to "id" to match the User entity's primary key column name
            childColumns = ["userId"], // Reflects the foreign key column in the Favorite entity
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"], // Updated to "id" to match the Recipe entity's primary key column name
            childColumns = ["recipeId"], // Reflects the foreign key column in the Favorite entity
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]), // Updated to "userId" to match the Favorite entity's field name
        Index(value = ["recipeId"]) // Updated to "recipeId" to match the Favorite entity's field name
    ]
)
data class Favorite(
    @PrimaryKey(autoGenerate = true) val favoriteID: Int = 0,
    val userId: Int, // Make sure this matches the naming convention used in the Favorite entity
    val recipeId: Int // Make sure this matches the naming convention used in the Favorite entity
)
