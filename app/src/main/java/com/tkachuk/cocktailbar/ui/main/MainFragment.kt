package com.tkachuk.cocktailbar.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.FragmentMainBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel
import com.tkachuk.cocktailbar.ui.searchbyingredient.SearchByIngredientActivity

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private var searchView: SearchView? = null

    private lateinit var binding: FragmentMainBinding
    private lateinit var ingredientsListViewModel: IngredientsListViewModel
    private lateinit var drinkListViewModel: DrinkListViewModel
    private lateinit var iMainActivity: IMainActivity

    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("draxvel", "MainFragment - onCreateView")

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        iMainActivity = activity as IMainActivity
        iMainActivity.setMainToolbar()

        binding.ingredientsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        ingredientsListViewModel = ViewModelProviders.of(this).get(IngredientsListViewModel::class.java)
        ingredientsListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, ingredientsListViewModel.errorClickListener) else hideError()
        })

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        drinkListViewModel.loadRandom10Drink(false)
        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener) else hideError()
        })

        drinkListViewModel.clickedDrinkId.observe(this, Observer { clickedDrinkId ->
            Log.d("draxvel", "observe click")
            val intent = Intent(activity, FullDrinkActivity::class.java)
            intent.putExtra("id", clickedDrinkId)
            startActivity(intent)
        })

        ingredientsListViewModel.clickedIngredientName.observe(this, Observer { clickedIngredientName ->
            val intent = Intent(activity, SearchByIngredientActivity::class.java)
            if (!clickedIngredientName.equals(getString(R.string.search_more))) {
                intent.putExtra("name", clickedIngredientName)
            }
            startActivity(intent)
        })

        binding.ingredientsListViewModel = ingredientsListViewModel

        binding.drinkList.clearOnScrollListeners()
        binding.drinkList.addOnScrollListener(InfiniteScrollListener(
                { drinkListViewModel.loadRandom10Drink(false) },
                linearLayoutManager))

        binding.drinkListViewModel = drinkListViewModel

        binding.swipeRefreshLayout.setOnRefreshListener {
            ingredientsListViewModel.loadIngredients()
            searchView?.isIconified = true
            drinkListViewModel.loadRandom10Drink(true)
            binding.drinkList.clearOnScrollListeners()
            binding.drinkList.addOnScrollListener(InfiniteScrollListener(
                    { drinkListViewModel.loadRandom10Drink(false) },
                    linearLayoutManager))
            binding.drinkList.smoothScrollToPosition(0)
        }

        return binding.root
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
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        searchView = menu?.findItem(R.id.search_item)?.actionView as SearchView

        searchView?.setIconifiedByDefault(true)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != "") {
                    drinkListViewModel.searchCocktails(query)
                    binding.drinkList.smoothScrollToPosition(0)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideError()
    }
}