package com.example.recipe.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.databinding.HomeRecyclerItemBinding
import com.example.recipe.db.entities.recipes.Recipe
import com.bumptech.glide.Glide

object RecipeComparator : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        // Recipe id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        // If the items have the same content, they are considered the same.
        return oldItem == newItem
    }
}

class RecipePagingDataAdapter(
    private val context: Context,
    private val listener: HomeListener
) :
    PagingDataAdapter<Recipe, RecipePagingDataAdapter.RecipeViewHolder>(RecipeComparator) {



    interface HomeListener {
        fun onRecipeClicked(recipe: Recipe)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = HomeRecyclerItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: HomeRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe?) {
            if (recipe == null) return

            Glide.with(context).load(recipe.imgSrc).into(binding.recipeImageView)
            binding.recipeTitleTextView.text = recipe.title
            binding.recipeDescriptionTextView.text = recipe.description
        }
    }
}

