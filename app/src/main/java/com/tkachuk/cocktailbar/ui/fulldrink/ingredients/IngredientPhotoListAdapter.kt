package com.tkachuk.cocktailbar.ui.fulldrink.ingredients

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ItemIngredientPhotoBinding
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkViewModel
import kotlinx.android.synthetic.main.item_ingredient_photo.view.*

class IngredientPhotoListAdapter(private val fullDrinkViewModel: FullDrinkViewModel) : RecyclerView.Adapter<IngredientPhotoListAdapter.ViewHolder>() {

    private var photoIngredientList: List<Ingredient> = listOf()
    private var isVisibleMeasure: Boolean = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ItemIngredientPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                R.layout.item_ingredient_photo, p0, false)
        return ViewHolder(binding)
    }

    fun setList(list: MutableList<Ingredient>, value: Boolean) {
        isVisibleMeasure  = value
        photoIngredientList = list.toList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photoIngredientList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(photoIngredientList[p1])
        p0.setVisibilityTextViewMeasure(isVisibleMeasure)

        p0.itemView.setOnClickListener {
            isVisibleMeasure = !isVisibleMeasure
            fullDrinkViewModel.clickedPhotoIngredient.value = isVisibleMeasure
        }
    }

    class ViewHolder(private val binding: ItemIngredientPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = IngredientPhotoViewModel()
        private val textViewMeasure = binding.root.textViewMeasure

        fun bind(ingredient: Ingredient) {
            viewModel.bind(ingredient)
            binding.ingredientPhotoViewModel = viewModel
        }

        fun setVisibilityTextViewMeasure(visible: Boolean) {
            if (visible) textViewMeasure.visibility = View.VISIBLE
            else textViewMeasure.visibility = View.GONE
        }
    }
}