package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipe.db.entities.favorites.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite): Long

    @Update
    suspend fun update(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE userId = :userId") // Updated to "userId"
    fun getFavoritesByUserId(userId: Int): LiveData<List<Favorite>> // Parameter name updated for consistency

    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId") // Updated to "recipeId"
    fun getFavoritesByRecipeId(recipeId: Int): LiveData<List<Favorite>> // Parameter name updated for consistency
}
