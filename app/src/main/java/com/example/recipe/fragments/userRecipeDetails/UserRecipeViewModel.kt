package com.example.recipe.fragments.userRecipeDetails

import android.util.Log
import android.widget.Toast
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

        loggedInUser =
            recipePreferencesRepository.getPreference(User::class.java, Constants.USER)
    }

    fun initRecipe(recipe: Recipe) {
        _recipe.value = recipe
        checkIfFavorite(recipe.id)
    }

    private fun loadRecipeById() {
        viewModelScope.launch {
            val result = recipe.value?.id?.let { recipeRepository.getRecipeById(it) }
            result?.let{_recipe.postValue(it)}
        }
    }

    private fun checkIfFavorite(recipeId: Int) {
        viewModelScope.launch {
            val isFav = favoriteRepository.isRecipeFavoriteByUser(recipeId, userId)
            _isFavorite.postValue(isFav)
        }
    }


    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            val currentStatus = _isFavorite.value ?: false
            Log.d("user id", userId.toString())
            Log.d("currentStatus", currentStatus.toString())
            if (currentStatus) {

                favoriteRepository.removeFavorite(recipeId, userId)
            } else {
                Log.d("random", recipeId.toString())
                favoriteRepository.addFavorite(recipeId, userId)
            }
            // After changing the status in the repository, check if the recipe is favorite again
            checkIfFavorite(recipeId)
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