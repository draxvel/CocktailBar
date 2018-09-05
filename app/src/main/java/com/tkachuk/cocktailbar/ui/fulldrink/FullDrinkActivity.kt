package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tkachuk.cocktailbar.R
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.tkachuk.cocktailbar.databinding.ActivityFullDrinkBinding

class FullDrinkActivity : AppCompatActivity() {

    val binding: ActivityFullDrinkBinding by lazy {
        DataBindingUtil.setContentView<ActivityFullDrinkBinding>(this, R.layout.activity_full_drink)
    }

    private lateinit var fullDrinkViewModel: FullDrinkViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fullDrinkViewModel = ViewModelProviders.of(this).get(FullDrinkViewModel::class.java)

        val id = intent.getIntExtra("id", 1)

        fullDrinkViewModel.loadRecipe(id)
        binding.fullDrinkViewModel = fullDrinkViewModel

        fullDrinkViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage)
            else hideError()
        })

        fullDrinkViewModel.drinkName.observe(this, Observer { title ->
            supportActionBar?.title = title
        })
        supportActionBar?.setHomeButtonEnabled(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}