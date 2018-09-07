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
import android.util.Log
import android.view.Menu
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ActivitySearchByIngredientBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_by_ingredient.*

class SearchByIngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchByIngredientBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private var errorSnackbar: Snackbar? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_ingredient)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_ingredient)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
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

        binding.drinkListViewModel = drinkListViewModel
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
        binding.textView.visibility = View.VISIBLE
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
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        if (errorMessage != R.string.not_found) {
            errorSnackbar?.setAction(R.string.retry, errorClickListener)
        }
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}