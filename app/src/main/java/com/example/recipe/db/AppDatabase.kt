package com.example.recipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.TypeConverters

import com.example.recipe.db.entities.favorites.Favorite
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.users.User
import com.example.recipe.db.entities.users.UserDao
import com.example.recipe.db.entities.favorites.FavoriteDao
import com.example.recipe.db.entities.recipes.RecipeDao

import com.example.recipe.utils.Converters

@Database(
    entities = [
        User::class,
        Recipe::class,

        Favorite::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe-app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}