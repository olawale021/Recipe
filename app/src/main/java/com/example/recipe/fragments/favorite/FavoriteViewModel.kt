package com.example.recipe.fragments.favorite

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.recipe.RecipeApplication
import com.example.recipe.db.RecipePreferencesRepository
import com.example.recipe.db.entities.favorites.Favorite
import com.example.recipe.db.entities.favorites.FavoriteRepository
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.recipes.RecipeRepository
import com.example.recipe.db.entities.users.User
import com.example.recipe.fragments.home.HomeViewModel
import com.example.recipe.utils.Constants
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val recipeRepository: RecipeRepository,
    recipePreferencesRepository: RecipePreferencesRepository
) : ViewModel() {

    val loggedInUser: LiveData<User?> =
        recipePreferencesRepository.getPreference(User::class.java, Constants.USER)

    fun getFavorites(user: User): LiveData<PagingData<Favorite>> =
            favoriteRepository.getFavoritesByUserId(user.id)
                .cachedIn(viewModelScope)




    internal suspend fun getRecipe(recipeId: Int): Recipe? {
            return recipeRepository.getRecipeById(recipeId)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
                val recipeRepository = application.container.recipeRepository
                val favoriteRepository = application.container.favoriteRepository
                val recipePreferencesRepository = application.recipePreferencesRepository
                FavoriteViewModel(
                    favoriteRepository = favoriteRepository,
                    recipeRepository = recipeRepository,
                    recipePreferencesRepository = recipePreferencesRepository
                )
            }
        }
    }
}
