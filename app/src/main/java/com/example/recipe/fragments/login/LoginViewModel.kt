package com.example.recipe.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipe.RecipeApplication
import com.example.recipe.db.RecipePreferencesRepository
import com.example.recipe.db.entities.users.User
import com.example.recipe.db.entities.users.UserRepository
import com.example.recipe.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginModel(
    var email: String = "",
    var password: String = "",
)

class LoginViewModel(
    private val userRepository: UserRepository,
    val recipePreferencesRepository: RecipePreferencesRepository,
) : ViewModel() {
    private var _loginModel: MutableStateFlow<LoginModel> = MutableStateFlow(LoginModel())
    val loginModel: StateFlow<LoginModel> = _loginModel.asStateFlow()

    suspend fun getUserDetails(email: String): User? {
        return userRepository.getUser(email = email)
    }

    fun resetLoginModel() {
        _loginModel.value = LoginModel()
    }
    suspend fun savePreferences(it: User) {
        recipePreferencesRepository.savePreference(Constants.USER, it)
        recipePreferencesRepository.savePreference(Constants.IS_LOGGED_IN, true)
    }

    suspend fun saveAdminPreferences() {
        recipePreferencesRepository.savePreference(Constants.IS_ADMIN, true)
        recipePreferencesRepository.savePreference(Constants.IS_LOGGED_IN, true)
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
                val userRepository = application.container.userRepository
                val recipePreferencesRepository = application.recipePreferencesRepository
                LoginViewModel(userRepository = userRepository, recipePreferencesRepository = recipePreferencesRepository)
            }
        }
    }
}