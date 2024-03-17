package com.example.recipe.db.entities.recipes

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

interface RecipeRepository {
    suspend fun insert(recipe: Recipe)
    fun getAllRecipes(pageSize: Int, filterText: String?): LiveData<PagingData<Recipe>>
    fun getRecipeById(recipeId: Int): LiveData<Recipe?>
}


class OfflineRecipeRepository(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    override fun getAllRecipes(
        pageSize: Int,
        filterText: String?
    ): LiveData<PagingData<Recipe>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = {
            recipeDao.getAllRecipes(filterText)
        }
    ).liveData

    override fun getRecipeById(recipeId: Int): LiveData<Recipe?> =
        recipeDao.getRecipeById(recipeId)
}

