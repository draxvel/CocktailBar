package com.tkachuk.cocktailbar.ui.searchbyingredient

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ActivitySearchByIngredientBinding
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel
import kotlinx.android.synthetic.main.activity_search_by_ingredient.*

class SearchByIngredientActivity : AppCompatActivity() {

    private val binding: ActivitySearchByIngredientBinding by lazy {
        DataBindingUtil.setContentView<ActivitySearchByIngredientBinding>(this, R.layout.activity_search_by_ingredient)
    }

    private lateinit var drinkListViewModel: DrinkListViewModel
    private lateinit var ingredientListViewModel: IngredientsListViewModel
    private var errorSnackBar: Snackbar? = null
    private var searchView: SearchView? = null
    private lateinit var toolbar: Toolbar

    private var ingredientsList : List<Ingredient> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        toolbar = binding.searchByIngredientToolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        ingredientListViewModel = ViewModelProviders.of(this).get(IngredientsListViewModel::class.java)

        ingredientListViewModel.loadIngredients(false)

        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener)
            else hideError()
        })

        clearList()
        val name = intent.getStringExtra("name")
        if (!name.isNullOrEmpty()) {
            searchByIngredient(name)
        } else {
            drinkListViewModel.setVisible(false)
        }

        drinkListViewModel.clickedDrinkId.observe(this, Observer { clickedDrinkId ->
            Log.d("draxvel", "observe click")
            val intent = Intent(this, FullDrinkActivity::class.java)
            intent.putExtra("id", clickedDrinkId)
            startActivity(intent)
        })

        ingredientListViewModel.clickedIngredientName.observe(this, Observer { clickedIngredientName ->
           searchByIngredient(clickedIngredientName!!)
        })

        binding.drinkListViewModel = drinkListViewModel
        binding.ingredientsListViewModel = ingredientListViewModel
        binding.ingredientsListSearch.visibility = View.VISIBLE
    }

    private fun searchByIngredient(name: String) {
        hideError()
        drinkListViewModel.searchByIngredient(name)
        binding.drinkList.visibility = View.VISIBLE
        binding.textView.visibility = View.INVISIBLE
        supportActionBar?.title = name
        binding.drinkList.smoothScrollToPosition(0)
    }

    private fun clearList() {
        hideError()
        searchView?.isIconified = true
        drinkListViewModel.drinkListAdapter.clear()
        supportActionBar?.title = getString(R.string.search)
        binding.drinkList.visibility = View.INVISIBLE
                //binding.ingredientsListSearch.visibility = View.VISIBLE
        binding.textView.visibility = View.INVISIBLE
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        searchView = menu?.findItem(R.id.search_by_ingredient)?.actionView as SearchView
        searchView?.setIconifiedByDefault(true)
        searchView?.queryHint = getString(R.string.search_by_ingredient)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != "") {
                    searchByIngredient(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val clearItem = menu.findItem(R.id.clear_item)
        clearItem.setOnMenuItemClickListener {
            if (textView.visibility == View.VISIBLE) {
                finish()
            }
            clearList()
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