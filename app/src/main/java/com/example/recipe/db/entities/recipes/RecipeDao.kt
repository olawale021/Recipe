package com.example.recipe.db.entities.recipes

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipe.db.entities.recipes.Recipe

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE recipeID = :recipeID")
    fun getRecipeById(recipeID: Int): LiveData<Recipe?>

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): LiveData<List<Recipe>>
}
