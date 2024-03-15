package com.example.recipe.db.entities.favorites

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipe.db.entities.favorites.Favorite
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM favorites WHERE userID = :userID")
    fun getFavoritesByUserId(userID: Int): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE recipeID = :recipeID")
    fun getFavoritesByRecipeId(recipeID: Int): LiveData<List<Favorite>>
}