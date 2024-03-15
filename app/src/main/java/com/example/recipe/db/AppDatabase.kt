package com.example.recipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.recipe.db.entities.categories.Category
import com.example.recipe.db.entities.favorites.Favorite
import com.example.recipe.db.entities.ingredients.Ingredient
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.users.User
import com.example.recipe.db.entities.users.UserDao
import com.example.recipe.db.entities.favorites.FavoriteDao
import com.example.recipe.db.entities.ingredients.IngredientDao
import com.example.recipe.db.entities.recipes.RecipeDao
import com.example.recipe.db.entities.categories.CategoryDao
import com.example.recipe.utils.Converters

@Database(
    entities = [
        User::class,
        Recipe::class,
        Ingredient::class,
        Category::class,
        Favorite::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun categoryDao(): CategoryDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipeapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
        fun createTestDatabase(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                .allowMainThreadQueries() // Allow queries on the main thread for testing purposes
                .build()
        }
    }
}