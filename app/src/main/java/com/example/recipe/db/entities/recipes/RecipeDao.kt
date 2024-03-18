package com.example.recipe.db.entities.recipes

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe?

    @Query("SELECT * FROM recipe WHERE servings > 0 AND LOWER(title) LIKE LOWER(:filterText)")
    fun getAvailableRecipes(filterText: String?): PagingSource<Int, Recipe>

    @Query("SELECT * FROM recipe WHERE servings > 0 AND id IN (:recipeIDs)")
    fun getRecipesByIDs(recipeIDs: List<Int>): PagingSource<Int, Recipe>


    @Query("SELECT * FROM recipe WHERE LOWER(title) LIKE LOWER(:filterText)")
    fun getAllRecipes(filterText: String?): PagingSource<Int, Recipe>

    @Query("SELECT * FROM recipe WHERE img_src = :imgSrc")
    suspend fun getRecipeByImageSrc(imgSrc: String): Recipe?
}
