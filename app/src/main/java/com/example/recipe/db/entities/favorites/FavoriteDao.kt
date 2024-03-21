package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite)

    // Assuming Favorite entity has a primary key, if not, you might need to adjust your strategy here
    // For the sake of consistency and based on the provided repository methods, you might not need an explicit update method unless you have specific use cases for it.

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId AND userId = :userId")
    suspend fun deleteFavorite(recipeId: Int, userId: Int)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoritesByUserId(userId: Int): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId")
    fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>>

    @Query("SELECT COUNT(*) > 0 FROM favorites WHERE recipeId = :recipeId AND userId = :userId")
    suspend fun isRecipeFavoriteByUser(recipeId: Int, userId: Int): Boolean

    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY favoriteID DESC")
    fun getFavoritesByUserIdPaged(userId: Int): PagingSource<Int, Favorite>
}
