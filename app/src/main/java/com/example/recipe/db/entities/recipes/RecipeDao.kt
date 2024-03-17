package com.example.recipe.db.entities.recipes

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


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

    @Query("SELECT * FROM recipes WHERE title LIKE :filterText OR ingredient LIKE :filterText")
    fun getAllRecipes(filterText: String?): PagingSource<Int, Recipe>
}