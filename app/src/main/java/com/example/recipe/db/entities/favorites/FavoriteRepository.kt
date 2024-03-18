package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData


interface FavoriteRepository {
    suspend fun insert(favorite: Favorite)
    fun getAllFavorites(): LiveData<List<Favorite>>
    // suspend fun getFavoriteById(favoriteId: Int): Favorite? // Uncomment if implemented in DAO
    fun getFavoritesByUserId(userId: Int): LiveData<List<Favorite>>
    fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>>
}


class OfflineFavoriteRepository(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override suspend fun insert(favorite: Favorite) {
        favoriteDao.insert(favorite)
    }

    override fun getAllFavorites(): LiveData<List<Favorite>> =
        favoriteDao.getAllFavorites()

    override fun getFavoritesByUserId(userId: Int): LiveData<List<Favorite>> =
        favoriteDao.getFavoritesByUserId(userId)

    override fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>> =
        favoriteDao.getFavoritesByRecipeId(recipeId)
}
