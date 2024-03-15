package com.example.recipe.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import com.example.recipe.RecipeApplication
import com.example.recipe.db.entities.users.User
import com.example.recipe.db.entities.users.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class RegisterModel(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
)

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _registerModel: MutableStateFlow<RegisterModel> = MutableStateFlow(RegisterModel())
    val registerModel: StateFlow<RegisterModel> = _registerModel.asStateFlow()

    suspend fun getUserDetails(email: String): User? {
        return userRepository.getUser(email = email)
    }

//    private val _uiMessage = MutableLiveData<String?>()
//    val uiMessage: LiveData<String?> = _uiMessage
//
//    fun registerUser() {
//        viewModelScope.launch {
//            try {
//                insertUser() // Your existing logic to insert user
//                _uiMessage.postValue("Registration successful!") // Post success message
//            } catch (e: Exception) {
//                _uiMessage.postValue("Registration failed: ${e.message}") // Post error message
//            }
//        }
//    }

    suspend fun insertUser() {
        val (
            username,
            email,
            password
        ) = registerModel.value
        userRepository.insert(
            user = User(
                username = username,
                email = email,
                password = password
            )
        )
    }

    fun resetRegisterModel() {
        _registerModel.value = RegisterModel()
    }
//    fun onToastShown() {
//        _uiMessage.value = null
//    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
                val userRepository = application.container.userRepository
                RegisterViewModel(userRepository = userRepository)
            }
        }
    }
}
