package com.tkachuk.cocktailbar.ui.searchbyingredient

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ActivitySearchByIngredientBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class SearchByIngredientActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySearchByIngredientBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_search_by_ingredient)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_ingredient)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener)
                else hideError()
        })

        binding.drinkListViewModel = drinkListViewModel
    }

    private fun showError(@StringRes errorMessage: Int, errorClickListener: View.OnClickListener) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        if (errorMessage != R.string.not_found) {
            errorSnackbar?.setAction(R.string.retry, errorClickListener)
        }
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
        swipeRefreshLayout.isRefreshing = false
    }
}