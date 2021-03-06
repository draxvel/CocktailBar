package com.tkachuk.cocktailbar.ui.categories.drinks

import android.arch.lifecycle.Observer
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
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.databinding.FragmentFilteredDrinksBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import com.tkachuk.cocktailbar.ui.main.IMainActivity

class FilteredDrinksFragment : Fragment() {

    lateinit var root: View
    lateinit var binding: FragmentFilteredDrinksBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private var errorSnackBar: Snackbar? = null
    private lateinit var toolbar: Toolbar
    private lateinit var iMainActivity: IMainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtered_drinks, container, false)
        root = binding.root
        iMainActivity = activity as IMainActivity

        var category = ""
        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getString("category", "Ordinary Drink")
        }

        toolbar = binding.fragmentFilteredDrinksToolbar
        toolbar.title = category
        val activity = activity as AppCompatActivity?
        activity?.supportActionBar?.hide()
        activity?.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d("draxvel", "setNavigationOnClickListener")
            activity?.supportActionBar?.hide()
            iMainActivity.setMainToolbar()
            activity?.supportActionBar?.show()
            fragmentManager?.popBackStack()
        }

        drinkListViewModel = DrinkListViewModel(DrinkRepository(activity!!.applicationContext))
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

    override fun onDestroy() {
        super.onDestroy()
        hideError()
        errorSnackBar = null
        System.gc()
    }
}