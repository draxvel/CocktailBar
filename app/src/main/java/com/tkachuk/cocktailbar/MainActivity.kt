package com.tkachuk.cocktailbar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import com.tkachuk.cocktailbar.databinding.ActivityMainBinding
import com.tkachuk.cocktailbar.ui.InfiniteScrollListener
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var searchView: SearchView? = null

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
            if (errorMessage != null) showError(errorMessage, ingredientsListViewModel.errorClickListener) else hideError()
        })

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener) else hideError()
        })

        binding.ingredientsListViewModel = ingredientsListViewModel

        binding.drinkList.clearOnScrollListeners()
        binding.drinkList.addOnScrollListener(InfiniteScrollListener(
                { drinkListViewModel.loadRandomDrink(false) },
                linearLayoutManager))

        binding.drinkListViewModel = drinkListViewModel

        swipeRefreshLayout.setOnRefreshListener {
            ingredientsListViewModel.loadIngredients()
            drinkListViewModel.loadRandomDrink(true)
        }
    }

    private fun showError(@StringRes errorMessage: Int, errorClickListener: View.OnClickListener) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        Log.d("draxvel", "hideSnack")
        errorSnackbar?.dismiss()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        searchView = menu?.findItem(R.id.search_item)?.actionView as SearchView

        searchView?.setIconifiedByDefault(true)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != "") {
                    drinkListViewModel.searchCocktails(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}