package com.tkachuk.cocktailbar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.tkachuk.cocktailbar.databinding.ActivityMainBinding
import com.tkachuk.cocktailbar.ui.InfiniteScrollListener
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ingredientsListViewModel: IngredientsListViewModel
    private lateinit var drinkListViewModel: DrinkListViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ingredientsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.ingredientsList2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.drinkList.layoutManager = linearLayoutManager
        ingredientsListViewModel = ViewModelProviders.of(this).get(IngredientsListViewModel::class.java)

        ingredientsListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
//        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
//            if (errorMessage != null) showError(errorMessage) else hideError()
//        })

        binding.ingredientsListViewModel = ingredientsListViewModel
        binding.drinkList.clearOnScrollListeners()
        binding.drinkList.addOnScrollListener(InfiniteScrollListener(
                { drinkListViewModel.loadRandomDrink() },
                linearLayoutManager))
        binding.drinkListViewModel = drinkListViewModel
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, ingredientsListViewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        Log.d("draxvel", "hideSnack")
        errorSnackbar?.dismiss()
    }
}