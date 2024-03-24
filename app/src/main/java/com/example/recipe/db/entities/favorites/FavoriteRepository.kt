package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData


interface FavoriteRepository {
    suspend fun addFavorite(recipeId: Int, userId: Int)
    suspend fun removeFavorite(recipeId: Int, userId: Int)
    suspend fun isRecipeFavoriteByUser(recipeId: Int, userId: Int): Boolean
    fun getAllFavorites(): LiveData<List<Favorite>>

    fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>>

    fun getFavoritesByUserId(userId: Int): LiveData<PagingData<Favorite>>
}


class OfflineFavoriteRepository(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addFavorite(recipeId: Int, userId: Int) {
        favoriteDao.addFavorite(Favorite(recipeId = recipeId, userId = userId))
    }

    override suspend fun removeFavorite(recipeId: Int, userId: Int) {
        favoriteDao.deleteFavorite(recipeId, userId)
    }

    override fun getAllFavorites(): LiveData<List<Favorite>> =
        favoriteDao.getAllFavorites()


    override fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>> =
        favoriteDao.getFavoritesByRecipeId(recipeId)

    override suspend fun isRecipeFavoriteByUser(recipeId: Int, userId: Int): Boolean =
        favoriteDao.isRecipeFavoriteByUser(recipeId, userId)

    override fun getFavoritesByUserId(userId: Int): LiveData<PagingData<Favorite>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { favoriteDao.getFavoritesByUserIdPaged(userId) }
        ).liveData
    }
}