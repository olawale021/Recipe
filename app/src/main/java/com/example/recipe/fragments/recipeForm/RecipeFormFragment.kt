package com.example.recipe.fragments.recipeForm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.recipe.RecipeApplication
import com.example.recipe.databinding.FragmentRecipeFormBinding
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.db.entities.recipes.RecipeRepository

import android.net.Uri

import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide


class RecipeFormFragment : Fragment() {
    private var _binding: FragmentRecipeFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeFormViewModel by viewModels {
        val application = requireActivity().application as RecipeApplication
        val repository = application.container.recipeRepository
        RecipeFormViewModelFactory(repository)
    }

    private val args: RecipeFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeFormBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        args.recipe?.let { viewModel.setEditableRecipe(it) }

        setupFormListeners()
        observeViewModel()

        return binding.root
    }

    private fun setupFormListeners() {
        binding.btnSubmit.setOnClickListener { submitForm() }
        binding.imgSrcEditText.doAfterTextChanged { text ->
            loadImageFromUrl(text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigateBack ->
            if (shouldNavigateBack) {
                Toast.makeText(context, "Recipe saved", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                // Reset the navigateBack value so it doesn't trigger again on reconfiguration
                viewModel.resetNavigateBack()
            }
        }
    }

    private fun submitForm() {
        val recipe = Recipe(
            id = args.recipe?.id ?: 0,
            title = binding.titleEditText.text.toString(),
            ingredients = binding.ingredientsEditText.text.toString(),
            description = binding.descriptionEditText.text.toString(),
            servings = binding.servingsEditText.text.toString(),
            totalTime = binding.totalTimeEditText.text.toString().toIntOrNull() ?: 0,
            imgSrc = binding.imgSrcEditText.text.toString(),
            pubDate = binding.pubDateEditText.text.toString(),
            cost = binding.costEditText.text.toString()
        )

        viewModel.saveRecipe(recipe)

        // Show a confirmation message
        Toast.makeText(context, "Recipe saved", Toast.LENGTH_SHORT).show()

        // Navigate back
        findNavController().popBackStack()
    }


    private fun loadImageFromUrl(url: String) {
        if (url.isNotEmpty()) {
            Glide.with(this)
                .load(url)
                .into(binding.recipeImageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

