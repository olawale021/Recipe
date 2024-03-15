package com.example.recipe.db.entities.categories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insert(category: Category)
    fun getAllCategories(): LiveData<List<Category>>
    suspend fun getCategoryById(categoryId: Int): LiveData<Category?>
}

class OfflineCategoryRepository(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    override fun getAllCategories(): LiveData<List<Category>> =
        categoryDao.getAllCategories()

    override suspend fun getCategoryById(categoryId: Int): LiveData<Category?> =
        categoryDao.getCategoryById(categoryId)
}
