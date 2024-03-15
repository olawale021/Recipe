package com.example.recipe.db.entities.recipes

import androidx.lifecycle.LiveData

interface RecipeRepository {
    suspend fun insert(recipe: Recipe)
    fun getAllRecipes(): LiveData<List<Recipe>>
    fun getRecipeById(recipeId: Int): LiveData<Recipe?>
}


class OfflineRecipeRepository(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    override fun getAllRecipes(): LiveData<List<Recipe>> =
        recipeDao.getAllRecipes()

    override fun getRecipeById(recipeId: Int): LiveData<Recipe?> =
        recipeDao.getRecipeById(recipeId)
}

