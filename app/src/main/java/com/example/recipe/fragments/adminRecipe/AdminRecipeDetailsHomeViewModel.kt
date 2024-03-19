package com.example.recipe.fragments.adminRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.recipes.RecipeRepository
import kotlinx.coroutines.launch

class AdminRecipeDetailsViewModel(private val recipeRepository: RecipeRepository, private val recipeId: Int) : ViewModel() {
    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> = _recipe

    init {
        loadRecipeById()
    }

    private fun loadRecipeById() {
        viewModelScope.launch {
            val result = recipeRepository.getRecipeById(recipeId)
            _recipe.postValue(result)
        }
    }
}

    class AdminRecipeDetailsViewModelFactory(
        private val repository: RecipeRepository,
        private val recipeId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminRecipeDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminRecipeDetailsViewModel(repository, recipeId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }