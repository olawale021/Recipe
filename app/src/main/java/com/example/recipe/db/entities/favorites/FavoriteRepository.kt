package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun insert(favorite: Favorite)
    fun getAllFavorites(): LiveData<List<Favorite>>
//    suspend fun getFavoriteById(favoriteId: Int): Favorite?
    fun getFavoritesByUserId(userID: Int): LiveData<List<Favorite>>
    fun getFavoritesByRecipeId(recipeID: Int): LiveData<List<Favorite>>
}


class OfflineFavoriteRepository(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override suspend fun insert(favorite: Favorite) {
        favoriteDao.insert(favorite)
    }

    override fun getAllFavorites(): LiveData<List<Favorite>> =
        favoriteDao.getAllFavorites()

//    override suspend fun getFavoriteById(favoriteId: Int): Favorite? {
//        // Assuming there's a method in your DAO to fetch a favorite by ID,
//        // but as your DAO doesn't include it in the snippet provided,
//        // you'll need to implement it in the DAO for this to work.
//        // This is a placeholder for the actual implementation.
//        return null // Implement this method in your DAO.
//    }

    override fun getFavoritesByUserId(userID: Int): LiveData<List<Favorite>> =
        favoriteDao.getFavoritesByUserId(userID)

    override fun getFavoritesByRecipeId(recipeID: Int): LiveData<List<Favorite>> =
        favoriteDao.getFavoritesByRecipeId(recipeID)
}
