package com.example.recipe.fragments.register

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recipe.R
import com.example.recipe.databinding.FragmentRegisterBinding
import com.example.recipe.utils.FormFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

        with(binding) {
            usernameEditText.requestFocus()
            emailEditText.doAfterTextChanged {
                FormFunctions.validateEmail(it.toString(), binding.emailLayout)
            }
            passwordEditText.doAfterTextChanged {
                FormFunctions.validatePassword(it.toString(), binding.passwordLayout)
            }
            confirmPasswordEditText.doAfterTextChanged {
                viewModel?.registerModel?.value?.password?.let { password ->
                    FormFunctions.validateConfirmPassword(
                        it.toString(),
                        password,
                        binding.confirmPasswordLayout
                    )
                }
            }
//            viewModel?.uiMessage?.observe(viewLifecycleOwner) { message ->
//                message?.let {
//                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                    viewModel?.onToastShown() // Ensure the message is reset to prevent repeated Toasts
//                }
//            }
            confirmPasswordEditText.setOnEditorActionListener { _, actionId, event ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    btnRegister.performClick()
                }
                false
            }
            btnRegister.setOnClickListener {
                val (
                    username,
                    email,
                    password,
                    confirmPassword
                ) = viewModel?.registerModel?.value ?: RegisterModel()
                val isFormValid = validateFields(
                    username = username,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )
                if (isFormValid) {
                    proceedToLoginScreen(email = email)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Some fields require your attention.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            existingUserAction.setOnClickListener {
                navigateToLoginScreen()
            }
        }

        return binding.root
    }

    private fun validateFields(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val isUsernameValid = FormFunctions.validateUsername(username, binding.usernameLayout)
        val isEmailValid = FormFunctions.validateEmail(email, binding.emailLayout)
        val isPasswordValid = FormFunctions.validatePassword(password, binding.passwordLayout)
        val isConfirmPasswordValid = FormFunctions.validateConfirmPassword(
            confirmPassword,
            password,
            binding.confirmPasswordLayout
        )

        return isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
    }


    private fun proceedToLoginScreen(email: String) {
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) { viewModel.getUserDetails(email = email) }
            if (user == null) {
                viewModel.insertUser()
                Toast.makeText(
                    requireContext(),
                    "Registration successful.",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToLoginScreen()
                viewModel.resetRegisterModel()
            } else {
                Toast.makeText(
                    requireContext(),
                    "User already exists!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(R.id.loginFragment)
    }
}
