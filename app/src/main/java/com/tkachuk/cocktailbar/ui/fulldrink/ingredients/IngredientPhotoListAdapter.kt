package com.tkachuk.cocktailbar.ui.fulldrink.ingredients

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ItemIngredientPhotoBinding
import com.tkachuk.cocktailbar.model.Ingredient

class IngredientPhotoListAdapter: RecyclerView.Adapter<IngredientPhotoListAdapter.ViewHolder>() {

    private var photoIngredientList: List<Ingredient> = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ItemIngredientPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                R.layout.item_ingredient_photo, p0, false)
        return ViewHolder(binding)
    }

    fun setList(list: MutableList<Ingredient>){
        photoIngredientList = list.toList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photoIngredientList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(photoIngredientList[p1])
        Log.d("draxve", "onBindViewHolder")
    }

    class ViewHolder(private val binding: ItemIngredientPhotoBinding): RecyclerView.ViewHolder(binding.root){
        private val viewModel = IngredientPhotoViewModel()

        fun bind(ingredient: Ingredient){
            if(ingredient.strIngredient1.isNotEmpty() && !ingredient.strIngredient1.equals("")){
                viewModel.bind(ingredient)
                Log.d("draxvel", ingredient.strIngredient1)
                binding.ingredientPhotoViewModel = viewModel
            }
        }
    }
}