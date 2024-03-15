package com.example.recipe

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.recipe.db.AppContainer
import com.example.recipe.db.RecipePreferencesRepository
import com.example.recipe.db.DefaultAppContainer

private const val RECIPE_PREFERENCE_NAME = "recipe_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = RECIPE_PREFERENCE_NAME
)

class RecipeApplication : Application() {
    lateinit var container: AppContainer
    lateinit var recipePreferencesRepository: RecipePreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        recipePreferencesRepository = RecipePreferencesRepository(dataStore)
    }
}
