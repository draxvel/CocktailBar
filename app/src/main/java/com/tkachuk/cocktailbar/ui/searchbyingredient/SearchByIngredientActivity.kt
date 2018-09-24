package com.tkachuk.cocktailbar.ui.searchbyingredient

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.databinding.ActivitySearchByIngredientBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel

class SearchByIngredientActivity : AppCompatActivity() {

    private val binding: ActivitySearchByIngredientBinding by lazy {
        DataBindingUtil.setContentView<ActivitySearchByIngredientBinding>(this, R.layout.activity_search_by_ingredient)
    }

    private lateinit var drinkListViewModel: DrinkListViewModel
    private lateinit var ingredientListViewModel: IngredientsListViewModel
    private var errorSnackBar: Snackbar? = null
    private var searchView: SearchView? = null
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        toolbar = binding.searchByIngredientToolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.drinkList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.ingredientsListSearch.layoutManager =  GridLayoutManager(this, 3)

        drinkListViewModel = DrinkListViewModel(DrinkRepository(applicationContext))
        ingredientListViewModel = ViewModelProviders.of(this).get(IngredientsListViewModel::class.java)

        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener)
            else hideError()
        })

        clearDrinkList()

        val name = intent.getStringExtra("name")
        if (!name.isNullOrEmpty()) {
            searchByIngredient(name)
        } else {
            ingredientListViewModel.loadIngredients(false)
        }

        drinkListViewModel.clickedDrinkId.observe(this, Observer { clickedDrinkId ->
            Log.d("draxvel", "observe click")
            val intent = Intent(this, FullDrinkActivity::class.java)
            intent.putExtra("id", clickedDrinkId)
            startActivity(intent)
        })

        ingredientListViewModel.clickedIngredientName.observe(this, Observer { clickedIngredientName ->
            Log.d("draxvel", "clickedIngredientName")
           searchByIngredient(clickedIngredientName!!)
        })

        binding.drinkListViewModel = drinkListViewModel
        binding.ingredientsListViewModel = ingredientListViewModel
        binding.ingredientsListSearch.visibility = View.VISIBLE
    }

    private fun searchByIngredient(name: String) {
        hideError()
        drinkListViewModel.searchByIngredient(name)
        binding.ingredientsListSearch.visibility = View.INVISIBLE
        binding.drinkList.visibility = View.VISIBLE
        supportActionBar?.title = name
        binding.drinkList.smoothScrollToPosition(0)

        searchView?.visibility = View.INVISIBLE
    }

    private fun clearDrinkList() {
        hideError()
        searchView?.isIconified = true
        drinkListViewModel.drinkListAdapter.clear()
        supportActionBar?.title = getString(R.string.search)
        binding.drinkList.visibility = View.INVISIBLE
        binding.ingredientsListSearch.visibility = View.VISIBLE
        searchView?.visibility = View.VISIBLE
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        searchView = menu?.findItem(R.id.search_by_ingredient)?.actionView as SearchView
        searchView?.setIconifiedByDefault(true)
        searchView?.queryHint = getString(R.string.search_by_ingredient)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText != "") {
                    ingredientListViewModel.search(newText)
                }
                return false
            }
        })

        val clearItem = menu.findItem(R.id.clear_item)
        clearItem.setOnMenuItemClickListener {
            ingredientListViewModel.loadIngredients(false)
            clearDrinkList()
            return@setOnMenuItemClickListener true
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ingredient, menu)
        return true
    }

    private fun showError(@StringRes errorMessage: Int, errorClickListener: View.OnClickListener) {
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        if (errorMessage != R.string.not_found) {
            errorSnackBar?.setAction(R.string.retry, errorClickListener)
        }
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }
}