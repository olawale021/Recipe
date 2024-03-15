package com.example.recipe.db.entities.ingredients
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipe.db.entities.ingredients.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    // Insert a new ingredient into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient: Ingredient): Long

    // Update an existing ingredient in the database
    @Update
    suspend fun update(ingredient: Ingredient)

    // Delete an ingredient from the database
    @Delete
    suspend fun delete(ingredient: Ingredient)

    // Query all ingredients in the database
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): LiveData<List<Ingredient>>

    // Query a single ingredient by its ID
    @Query("SELECT * FROM ingredients WHERE ingredientID = :ingredientId")
    fun getIngredientById(ingredientId: Int): LiveData<Ingredient?>

    // Optional: Delete all ingredients (might not be needed in every app)
    @Query("DELETE FROM ingredients")
    suspend fun deleteAllIngredients()
}