package com.example.recipe.fragments.adminHome

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe.databinding.AdminHomeRecyclerItemBinding
import com.example.recipe.db.entities.recipes.Recipe

object RecipeComparator : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}

class AdminHomePagingDataAdapter(
    private val context: Context,
    private val listener: HomeListener,
) :
    PagingDataAdapter<Recipe, AdminHomePagingDataAdapter.HomeViewHolder>(RecipeComparator) {
    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = AdminHomeRecyclerItemBinding.inflate(inflater, parent, false)

        return HomeViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item, position)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a Product object.
    inner class HomeViewHolder(private val binding: AdminHomeRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe?, position: Int) {
            try {
                if (recipe == null) {
                    return
                }

                recipe.imgSrc?.let {
                    Glide.with(context).load(it)
                        .into(binding.memberImageView)
                }
                //setup cart qty control
                binding.editRecipeButton.setOnClickListener {
                    listener.editRecipe(recipe = recipe)
                }
                binding.deleteRecipeButton.setOnClickListener {
                    listener.deleteRecipe(recipe = recipe)
                }

                binding.titleTextView.text = recipe.title
                binding.categoryTextView.text = recipe.category
                binding.totalTimeTextView.text = recipe.totalTime.toString()
//                binding.servingsTextView.text = recipe.servings.toString()

                binding.infoImageView.setOnClickListener {
                    recipe?.id?.let { id ->
                        listener.viewRecipeDetails(id)
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface HomeListener {
        fun editRecipe(recipe: Recipe)
        fun deleteRecipe(recipe: Recipe)
        fun viewRecipeDetails(recipeId: Int)
    }
}