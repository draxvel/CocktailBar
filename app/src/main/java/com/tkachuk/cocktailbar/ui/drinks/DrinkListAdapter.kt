package com.tkachuk.cocktailbar.ui.drinks

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ItemDrinkBinding
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity

class DrinkListAdapter : RecyclerView.Adapter<DrinkListAdapter.ViewHolder>() {

    private var drinkList: List<Drink> = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ItemDrinkBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                R.layout.item_drink, p0, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(drinkList[p1])
        p0.itemView.setOnClickListener {
            p0.showFullDrink()
        }
    }

    fun addToList(result: MutableList<Drink>) {
        drinkList += result.toList()
        notifyDataSetChanged()
    }

    fun updateList(result: List<Drink>) {
        drinkList = result
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = DrinkViewModel()
        private val context = binding.root.context

        fun bind(drink: Drink) {
            viewModel.bind(drink)
            binding.drinkViewModel = viewModel
        }

        fun showFullDrink(){
            context.startActivity(Intent(context, FullDrinkActivity::class.java))
        }
    }
}
