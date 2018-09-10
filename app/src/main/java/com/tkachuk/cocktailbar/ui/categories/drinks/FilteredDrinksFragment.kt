package com.tkachuk.cocktailbar.ui.categories.drinks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.FilteredDrinksFragmentBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel

class FilteredDrinksFragment : Fragment() {

    lateinit var root: View
    lateinit var binding: FilteredDrinksFragmentBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.filtered_drinks_fragment, container, false)
        root = binding.root

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        var category = ""
        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getString("category", "Ordinary Drink")
        }

        drinkListViewModel.loadFilteredDrinks(category)

        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener) else hideError()
        })

        binding.drinkListViewModel = drinkListViewModel
        return root
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