package com.example.recipe.db.entities.ingredients

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import com.example.recipe.db.entities.ingredients.IngredientDao

interface IngredientRepository {
    suspend fun insert(ingredient: Ingredient)
    fun getAllIngredients(): LiveData<List<Ingredient>>
    fun getIngredientById(ingredientId: Int): LiveData<Ingredient?>
}



class OfflineIngredientRepository(
    private val ingredientDao: IngredientDao
) : IngredientRepository {
    override suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }

    override fun getAllIngredients(): LiveData<List<Ingredient>> =
        ingredientDao.getAllIngredients()

    override fun getIngredientById(ingredientId: Int): LiveData<Ingredient?> =
        ingredientDao.getIngredientById(ingredientId)
}
