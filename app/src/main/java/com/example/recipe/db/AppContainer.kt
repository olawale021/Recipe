package com.example.recipe.db


import android.content.Context
import com.example.recipe.db.AppDatabase
import com.example.recipe.db.entities.categories.CategoryRepository
import com.example.recipe.db.entities.categories.OfflineCategoryRepository
import com.example.recipe.db.entities.recipes.RecipeRepository
import com.example.recipe.db.entities.recipes.OfflineRecipeRepository
import com.example.recipe.db.entities.users.UserRepository
import com.example.recipe.db.entities.users.OfflineUserRepository
import com.example.recipe.db.entities.favorites.FavoriteRepository
import com.example.recipe.db.entities.favorites.OfflineFavoriteRepository


interface AppContainer {
    val userRepository: UserRepository
    val recipeRepository: RecipeRepository
    val categoryRepository: CategoryRepository
    val favoriteRepository: FavoriteRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database by lazy { AppDatabase.getDatabase(context) }


    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getDatabase(context = context).userDao())
    }

    override val recipeRepository: RecipeRepository by lazy {
        OfflineRecipeRepository(AppDatabase.getDatabase(context = context).recipeDao())
    }


    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(AppDatabase.getDatabase(context = context).categoryDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        OfflineFavoriteRepository(AppDatabase.getDatabase(context = context).favoriteDao())
    }
}