package com.example.recipe.fragments.userRecipeDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipe.R
import com.example.recipe.RecipeApplication
import com.example.recipe.databinding.FragmentUserRecipeBinding
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.utils.Constants
import kotlinx.coroutines.launch

class UserRecipeFragment : Fragment() {
    private lateinit var binding: FragmentUserRecipeBinding

    private val args: UserRecipeFragmentArgs by navArgs()

    // Declare viewModel as nullable or lateinit if you prefer
    private var viewModel: UserRecipeDetailsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRecipeBinding.inflate(inflater, container, false)

        initViewModel()

        viewModel?.initRecipe(args.recipe)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewModel = viewModel


        binding.addFavoriteImageView.setOnClickListener {
            Log.d("text", viewModel?.recipe?.value.toString())
            viewModel?.recipe?.value?.id?.let { it1 -> viewModel?.toggleFavorite(it1) }
        }
        return binding.root
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            // Directly access RecipePreferencesRepository instance from the application
            val app = requireActivity().application as RecipeApplication
            val recipePreferencesRepository = app.recipePreferencesRepository
            val userId = app.recipePreferencesRepository.getPreferenceNow(Int::class.java, Constants.USER_ID) ?: -1

            // Ensure you handle the case where userId might not be found (-1 or null)
            // Initialize the ViewModel now that you have the userId
            val recipeRepository = app.container.recipeRepository
            val favoriteRepository = app.container.favoriteRepository

            // Create the ViewModelFactory with all necessary parameters
            val factory = UserRecipeDetailsViewModelFactory(recipeRepository, favoriteRepository, userId, recipePreferencesRepository)

            // Initialize your ViewModel
            viewModel = ViewModelProvider(this@UserRecipeFragment, factory).get(UserRecipeDetailsViewModel::class.java)
            setupObservers()
        }
    }

    private fun setupObservers() {
        viewModel?.recipe?.observe(viewLifecycleOwner) { recipe ->
            recipe?.let { updateUI(recipe) }
        }
        viewModel?.isFavorite?.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.addFavoriteImageView.setImageResource(R.drawable.add_to_favoritemarked) // Your "filled" icon
            } else {
                binding.addFavoriteImageView.setImageResource(R.drawable.add_favorite) // Your "border" icon
            }
        }
    }

    private fun updateUI(recipe: Recipe) {
        with(binding) {
            Glide.with(this@UserRecipeFragment)
                .load(recipe.imgSrc)
                .placeholder(R.drawable.no_image)
                .into(recipeImageView)
            Log.d("MainActivity", recipe.title)
            titleTextView.text = recipe.title
            categoryTextView.text = recipe.category
            ingredientsTextView.text = recipe.ingredients
            instructionTextView.text = recipe.instruction
            totalTimeTextView.text = getString(R.string.total_time_format, recipe.totalTime.toString()) // Assuming totalTime is an Int representing minutes
            servingsTextView.text = getString(R.string.servings_format, recipe.servings) // Assuming servings is an Int
        }

    }
}
