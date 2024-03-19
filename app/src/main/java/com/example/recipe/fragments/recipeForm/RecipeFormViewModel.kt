package com.example.recipe.fragments.recipeForm

import androidx.lifecycle.*
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.recipes.RecipeRepository
import kotlinx.coroutines.launch

class RecipeFormViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _recipeId = MutableLiveData<Int>()
    val recipe: LiveData<Recipe?> = _recipeId.switchMap { id ->
        liveData { emit(recipeRepository.getRecipeById(id)) }
    }

    private val _editableRecipe = MutableLiveData<Recipe?>()
    val editableRecipe: LiveData<Recipe?> = _editableRecipe

    fun setEditableRecipe(recipe: Recipe?) {
        _editableRecipe.value = recipe
    }

    // LiveData for UI state
    val title = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val servings = MutableLiveData<String>()
    val totalTime = MutableLiveData<String>()
    val imgSrc = MutableLiveData<String>()
    val pubDate = MutableLiveData<String>()
    val cost = MutableLiveData<String>()

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    fun setRecipeId(recipeId: Int) {
        _recipeId.value = recipeId
    }

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    fun resetNavigateBack() {
        _navigateBack.value = false
    }



    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.id > 0) {
                recipeRepository.update(recipe)
            } else {
                recipeRepository.insert(recipe)
            }
            _navigateBack.value = true
        }
    }
}

class RecipeFormViewModelFactory(private val recipeRepository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeFormViewModel(recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
