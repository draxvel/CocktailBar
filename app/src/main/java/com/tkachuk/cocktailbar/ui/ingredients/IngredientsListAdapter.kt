package com.tkachuk.cocktailbar.ui.ingredients

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ItemIngredientBinding
import com.tkachuk.cocktailbar.model.Ingredient

class IngredientsListAdapter: RecyclerView.Adapter<IngredientsListAdapter.ViewHolder>(){

    private lateinit var ingredientsList: List<Ingredient>

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ItemIngredientBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                R.layout.item_ingredient, p0, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(::ingredientsList.isInitialized) ingredientsList.size else 0
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(ingredientsList[p1])
    }

    fun updateList(result: List<Ingredient>) {
        ingredientsList = result
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemIngredientBinding): RecyclerView.ViewHolder(binding.root){

        private val viewModel = IngredientViewModel()

        fun bind(ingredient: Ingredient){
            viewModel.bind(ingredient)
            binding.viewModel = viewModel
        }
    }

}