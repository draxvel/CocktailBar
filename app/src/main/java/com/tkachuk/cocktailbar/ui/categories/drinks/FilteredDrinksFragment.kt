package com.tkachuk.cocktailbar.ui.categories.drinks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.FragmentFilteredDrinksBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity

class FilteredDrinksFragment : Fragment() {

    lateinit var root: View
    lateinit var binding: FragmentFilteredDrinksBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private var errorSnackBar: Snackbar? = null
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtered_drinks, container, false)
        root = binding.root

        var category = ""
        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getString("category", "Ordinary Drink")
        }

        toolbar = binding.fragmentFilteredDrinksToolbar
        toolbar.title = category
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d("draxvel", "setNavigationOnClickListener")
            fragmentManager?.popBackStack()
        }

        drinkListViewModel = ViewModelProviders.of(this).get(DrinkListViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        drinkListViewModel.loadFilteredDrinks(category)

        drinkListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, drinkListViewModel.errorClickListener) else hideError()
        })

        drinkListViewModel.clickedDrinkId.observe(this, Observer { clickedDrinkId ->
            Log.d("draxvel", "observe click")
            val intent = Intent(activity, FullDrinkActivity::class.java)
            intent.putExtra("id", clickedDrinkId)
            startActivity(intent)
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