package com.example.recipe.fragments.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe.databinding.FavoriteRecyclerItemBinding
import com.example.recipe.db.entities.favorites.Favorite
import com.example.recipe.db.entities.recipes.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FavoriteComparator : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem.favoriteID == newItem.favoriteID
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}

class FavoritePagingDataAdapter(
    private val context: Context,
    private val listener: FavoriteListener,
) : PagingDataAdapter<Favorite, FavoritePagingDataAdapter.FavoriteViewHolder>(FavoriteComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = getItem(position)
        favorite?.let {
            holder.bind(it, listener)
        }
    }

    class FavoriteViewHolder(private val binding: FavoriteRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite, listener: FavoriteListener) {
            CoroutineScope(Dispatchers.Main).launch {
                val recipe = listener.getRecipe(favorite.recipeId)
                recipe?.let {
                    Glide.with(binding.memberImageView.context).load(it.imgSrc).into(binding.memberImageView)
                    binding.titleTextView.text = it.title
                    binding.categoryTextView.text = it.category
                    binding.totalTimeTextView.text = "${it.totalTime} mins"


                    binding.removeFromFavorites.setOnClickListener {
                        // Pass the favoriteID, which is the unique identifier for the favorite entity
                        listener.removeFromFavorites(favorite.favoriteID)
                    }
                }
            }



        }
    }

    interface FavoriteListener {
        suspend fun getRecipe(recipeId: Int): Recipe?
        fun removeFromFavorites(favoriteId: Int)
    }
}