package com.example.recipe.fragments.adminHome


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import androidx.paging.LoadState

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.recipe.R
import com.example.recipe.databinding.FragmentAdminHomeBinding
import com.example.recipe.db.entities.recipes.Recipe

import com.example.recipe.utils.SearchableFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class AdminHomeFragment : SearchableFragment<Recipe>(),
    AdminHomePagingDataAdapter.HomeListener {

    companion object {
        private val TAG = AdminHomeFragment::getTag
    }

    private val viewModel: AdminHomeViewModel by viewModels { AdminHomeViewModel.Factory }
    private lateinit var binding: FragmentAdminHomeBinding
    private lateinit var adapter: AdminHomePagingDataAdapter
    override var viewModelFilterText: String? = null
    override var searchCallback: ((String) -> Unit)? = null
    override var searchButton: ImageButton? = null
    override var searchText: TextInputEditText? = null

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

//        searchButton = binding.imageButtonStopSearch
        searchText = binding.etSearch
        setOnBackPressedCallback()
    }

    override fun initCompulsoryVariables() {
        viewModelFilterText = viewModel.mFilterText
        searchCallback = { it -> viewModel.search(it) }
    }

    override fun returnBindingRoot(): View {
        return binding.root
    }

    override fun setBinding() {
        setupStatusFilter()
        binding.btnRefresh.setOnClickListener { refresh() }
        binding.addNewBtn.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections.actionAdminHomeFragmentToRecipeFormFragment(
                    null
                )
            )
        }
    }

    override fun initRecycler() {
        adapter = AdminHomePagingDataAdapter(requireContext(), this) // Ensure this fits your implementation

        // Observe the LiveData or Flow from the ViewModel
        lifecycleScope.launch {
            viewModel.recipes.observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }

        // Monitor the adapter's loading state for any changes
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only interested in the refresh component of the load state
                .map { it.refresh }
                .distinctUntilChanged()
                .collect { loadState ->
                    when (loadState) {
                        is LoadState.NotLoading -> {
                            // Data has finished loading, perform any relevant UI updates here
                            // For example, hide a progress indicator
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
//                            binding.noOfResultsTextview.visibility = View.GONE
                        }
                        is LoadState.Loading -> {
                            // Data is loading, you might want to show a loading spinner or a progress bar
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            // An error occurred while loading data, you might want to show an error message
                            // or retry button
                            Toast.makeText(context, "Error loading recipes", Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
        }

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

//    override fun setSearchResult(listSize: Int) {
//        binding.noOfResultsTextview.visibility =
//            View.VISIBLE
//        val size = if (listSize >= 30) "$listSize+" else listSize.toString()
//        binding.noOfResultsTextview.text = getString(R.string.y_results, size)
//    }

    private fun refresh() {
        viewModel.insertRecipes()
    }

    /**
     * Sets up status filter drop down, actions to be performed after item selection,
     * method to handle end icon to display, icon actions
     */
    private fun setupStatusFilter() {
        // set adapter
//        binding.acvFilter.setAdapter(
//            getAdapter(
//                listOf(
//                    getString(R.string.fiction),
//                    getString(R.string.non_fiction),
//                ).toTypedArray()
//            )
//        )

        // set text changed listener
//        binding.acvFilter.doOnTextChanged { text, _, _, _ ->
//            binding.shouldCancelShow = !text.isNullOrEmpty()
//        }
//
//        binding.acvFilter.setOnClickListener {
//            binding.acvFilter.showDropDown()
//        }
//
//        binding.acvFilter.setOnItemClickListener { _, _, _, _ ->
//            viewModel.selectedCategory = binding.acvFilter.text.toString()
//            initRecycler()
//        }
//
//        binding.acvFilter.doOnTextChanged { text, _, _, _ ->
//            binding.shouldCancelShow = !text.isNullOrEmpty()
//        }
//
//        // set icon handlers
//        binding.ivFilterDropDown.setOnClickListener {
//            binding.acvFilter.showDropDown()
//        }
//        binding.ivFilterCancel.setOnClickListener {
//            binding.acvFilter.text = null
//            viewModel.selectedCategory = ""
//            initRecycler()
//        }
    }

    /**
     * Get Adapter object based on the entered string array
     */
    private fun getAdapter(stringArr: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line, stringArr
        )
    }


    override fun editRecipe(recipe: Recipe) {
//        findNavController().navigate(
//            AdminHomeFragmentDirections.actionAdminHomeFragmentToProductFormFragment(
//                recipe
//            )
//        )
    }

    override fun deleteRecipe(recipe: Recipe) {
        viewModel.deleteRecipe(recipe = recipe)
    }

    override fun viewRecipeDetails(recipeId: Int) {
        val action = AdminHomeFragmentDirections.actionAdminHomeFragmentToAdminRecipeDetailsFragment(recipeId)
        findNavController().navigate(action)
    }
    private fun setOnBackPressedCallback() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                    exitProcess(0)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}