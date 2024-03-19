package com.example.recipe.fragments.adminRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipe.R
import com.example.recipe.RecipeApplication
import com.example.recipe.databinding.FragmentAdminRecipeDetailsBinding
import com.example.recipe.db.entities.recipes.Recipe

class AdminRecipeDetailsFragment : Fragment() {
    private var _binding: FragmentAdminRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: AdminRecipeDetailsFragmentArgs by navArgs()

    private val viewModel: AdminRecipeDetailsViewModel by viewModels {
        val application = requireActivity().application as RecipeApplication
        val recipeRepository = application.container.recipeRepository
        AdminRecipeDetailsViewModelFactory(recipeRepository, args.recipeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminRecipeDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AdminRecipeDetailsFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let { updateUI(it) }
        }
    }

    private fun updateUI(recipe: Recipe) {
        with(binding) {
            Glide.with(this@AdminRecipeDetailsFragment)
                .load(recipe.imgSrc)
                .placeholder(R.drawable.no_image)
                .into(recipeImageView)
            // Other UI updates handled via data binding
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
