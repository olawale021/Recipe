package com.example.recipe.fragments.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe.R
import com.example.recipe.databinding.FragmentAdminHomeBinding
import com.example.recipe.databinding.FragmentFavoriteBinding
import com.example.recipe.db.entities.recipes.Recipe
import com.example.recipe.fragments.adminHome.AdminHomePagingDataAdapter
import com.example.recipe.fragments.home.HomeViewModel

class FavoriteFragment : Fragment(), FavoritePagingDataAdapter.FavoriteListener {

    private val viewModel: FavoriteViewModel by viewModels { FavoriteViewModel.Factory }
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoritePagingDataAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initRecycler()
        return binding.root
    }

    fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

    }

    fun initRecycler() {
        adapter = FavoritePagingDataAdapter(requireContext(), this)
        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.getFavorites(it).observe(viewLifecycleOwner) { pagingData ->
                    adapter.submitData(lifecycle, pagingData)
                }
            }
        }
        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override suspend fun getRecipe(recipeId: Int): Recipe? {
        return viewModel.getRecipe(recipeId)
    }

    override fun removeFromFavorites(favoriteId: Int) {
        TODO("Not yet implemented")
    }
}