package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.recipe.utils.Constants
import androidx.lifecycle.ViewModelProvider
import com.example.recipe.fragments.login.LoginViewModel
import com.example.recipe.utils.Constants.IS_LOGGED_IN


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, LoginViewModel.Factory)[LoginViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val navGraph = inflater.inflate(R.navigation.nav_graph)

//        createDirs()
//
        val isLoggedIn = viewModel.recipePreferencesRepository.getPreference(
            Boolean::class.java,
            IS_LOGGED_IN
        )
        val isAdmin = viewModel.recipePreferencesRepository.getPreference(
            Boolean::class.java,
            Constants.IS_ADMIN
        )
        isLoggedIn.observe(this@MainActivity) {
            if (it == true) {
                isAdmin.observe(this@MainActivity) { admin ->
                    Log.d("AppActivity", admin.toString())
                    if (admin == true) {
                        val intent = Intent(this@MainActivity, AdminActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@MainActivity, UserActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                navGraph.setStartDestination(startDestId = R.id.loginFragment)
            }
            val navController = navHostFragment.navController
            navController.setGraph(navGraph, intent.extras)
        }
    }

//    private fun createDirs() {
//        try {
//            createMediaDirectory(
//                this,
//                Constants.PRODUCT_PICTURE_DIR,
//                Environment.DIRECTORY_PICTURES
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}

