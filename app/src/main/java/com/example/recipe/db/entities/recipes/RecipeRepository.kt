package com.example.recipe.db.entities.recipes

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.recipe.db.entities.recipes.RecipeDao

interface RecipeRepository {
    suspend fun insert(recipe: Recipe)
    suspend fun update(recipe: Recipe)
    suspend fun delete(recipe: Recipe)
    suspend fun getRecipeById(recipeId: Int): Recipe?
    fun getAvailableRecipe(pageSize: Int, filterText: String?): LiveData<PagingData<Recipe>>
    fun getRecipesByIDs(pageSize: Int, recipeIDs: List<Int>): LiveData<PagingData<Recipe>>
    fun getAllRecipes(pageSize: Int, filterText: String?): LiveData<PagingData<Recipe>>
    suspend fun getRecipeByImageSrc(imgSrc: String): Recipe?
}

class OfflineRecipeRepository(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe = recipe)
    }

    override suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe = recipe)
    }

    override suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe = recipe)
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe? =
        recipeDao.getRecipeById(recipeId = recipeId)

    override fun getAvailableRecipe(
        pageSize: Int,
        filterText: String?
    ): LiveData<PagingData<Recipe>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = {
            recipeDao.getAvailableRecipes(filterText)
        }
    ).liveData

    override fun getRecipesByIDs(
        pageSize: Int,
        recipeIDs: List<Int>
    ): LiveData<PagingData<Recipe>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = {
            recipeDao.getRecipesByIDs(recipeIDs)
        }
    ).liveData


    override fun getAllRecipes(
        pageSize: Int,
        filterText: String?
    ): LiveData<PagingData<Recipe>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = {
            recipeDao.getAllRecipes(filterText)
        }
    ).liveData

    override suspend fun getRecipeByImageSrc(imgSrc: String): Recipe? =
        recipeDao.getRecipeByImageSrc(imgSrc = imgSrc)
}