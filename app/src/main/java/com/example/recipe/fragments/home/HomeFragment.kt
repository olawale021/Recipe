package com.example.recipe.fragments.home

import com.example.recipe.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe.databinding.FragmentHomeBinding
import com.example.recipe.db.entities.recipes.Recipe
import kotlinx.coroutines.launch
import com.example.recipe.utils.SearchableFragment
import com.google.android.material.textfield.TextInputEditText
import kotlin.system.exitProcess
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class HomeFragment : SearchableFragment<Recipe>(), RecipePagingDataAdapter.HomeListener {

    companion object {
        private val TAG = HomeFragment::getTag
    }

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecipePagingDataAdapter
    override var viewModelFilterText: String? = null
    override var searchCallback: ((String) -> Unit)? = null
    override var searchButton: ImageButton? = null
    override var searchText: TextInputEditText? = null

    override fun onRecipeClicked(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

        searchButton = binding.imageButtonStopSearch
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
        binding.btn.setOnClickListener { refresh() }
    }

    override fun initRecycler() {
        adapter = RecipePagingDataAdapter(requireContext(), this) // Ensure this fits your implementation

        // Observe the LiveData or Flow from the ViewModel
        lifecycleScope.launch {
            viewModel.recipe.observe(viewLifecycleOwner) { pagingData ->
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
                            binding.noOfResultsTextview.visibility = View.GONE
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


    private fun getAdapter(stringArr: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line, stringArr
        )
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

    private fun refresh() {
        viewModel.insertRecipes()
    }


    private fun setupStatusFilter() {
        // set adapter
        binding.acvFilter.setAdapter(
            getAdapter(
                listOf(
                    getString(R.string.fiction),
                    getString(R.string.non_fiction),
                ).toTypedArray()
            )
        )
        // set text changed listener
        binding.acvFilter.doOnTextChanged { text, _, _, _ ->
            binding.shouldCancelShow = !text.isNullOrEmpty()
        }

        binding.acvFilter.setOnClickListener {
            binding.acvFilter.showDropDown()
        }

        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            val welcomeMessage = if (user != null && user.username.isNotEmpty()) {
                // Update the welcome message with the user's name
                getString(R.string.welcome_message_with_name, user.username)
            } else {
                // Fallback message in case there's no user name available
                getString(R.string.welcome_guest)
            }
            binding.textViewWelcome.text = welcomeMessage
        }

//        binding.acvFilter.setOnItemClickListener { _, _, i, l ->
//            viewModel.selectedCategory = binding.acvFilter.text.toString()
//            initRecycler()
//        }

        binding.acvFilter.doOnTextChanged { text, _, _, _ ->
            binding.shouldCancelShow = !text.isNullOrEmpty()
        }

        // set icon handlers
        binding.ivFilterDropDown.setOnClickListener {
            binding.acvFilter.showDropDown()
        }
//        binding.ivFilterCancel.setOnClickListener {
//            binding.acvFilter.text = null
//            viewModel.selectedCategory = ""
//            initRecycler()
//        }
    }

}
