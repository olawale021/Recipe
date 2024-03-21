package com.example.recipe.fragments.userRecipeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipe.db.RecipePreferencesRepository
import com.example.recipe.db.entities.favorites.FavoriteRepository
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.recipes.RecipeRepository
import com.example.recipe.db.entities.users.User
import com.example.recipe.utils.Constants
import kotlinx.coroutines.launch

class UserRecipeDetailsViewModel(
    private val recipeRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository, // Inject FavoriteRepository
    private val userId: Int,
    recipePreferencesRepository: RecipePreferencesRepository
) : ViewModel() {
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe
    private lateinit var loggedInUser: LiveData<User?>

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    init {
        loadRecipeById()
        checkIfFavorite()
        loggedInUser =
            recipePreferencesRepository.getPreference(User::class.java, Constants.USER)
    }

    fun initRecipe(recipe: Recipe) {
        _recipe.value = recipe
    }

    private fun loadRecipeById() {
        viewModelScope.launch {
            val result = recipe.value?.id?.let { recipeRepository.getRecipeById(it) }
            result?.let{_recipe.postValue(it)}
        }
    }

    private fun checkIfFavorite() {
        viewModelScope.launch {
            val isFav = recipe.value?.id?.let { favoriteRepository.isRecipeFavoriteByUser(it, userId) }
            isFav?.let { _isFavorite.postValue(it)}
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentStatus = _isFavorite.value ?: false
            if (currentStatus) {
                // It's currently a favorite, so remove it
                recipe.value?.id?.let { favoriteRepository.removeFavorite(it, userId) }
            } else {
                // It's not a favorite, so add it
                recipe.value?.id?.let { favoriteRepository.addFavorite(it, userId) }
            }
            // Update the live data to reflect the change
            _isFavorite.postValue(!currentStatus)
        }
    }
}


class UserRecipeDetailsViewModelFactory(
    private val repository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userId: Int,
    private val recipePreferencesRepository: RecipePreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRecipeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserRecipeDetailsViewModel(repository, favoriteRepository, userId, recipePreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}