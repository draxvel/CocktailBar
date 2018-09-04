package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tkachuk.cocktailbar.R
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.databinding.ActivityFullDrinkBinding

class FullDrinkActivity: AppCompatActivity() {

    val binding: ActivityFullDrinkBinding by lazy{
        DataBindingUtil.setContentView<ActivityFullDrinkBinding>(this, R.layout.activity_full_drink)
    }

    private lateinit var fullDrinkViewModel: FullDrinkViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("draxvel", "onCreate")

        fullDrinkViewModel = ViewModelProviders.of(this).get(FullDrinkViewModel::class.java)

        val id = intent.getIntExtra("id", 1)

        fullDrinkViewModel.loadRecipe(id)
        binding.fullDrinkViewModel = fullDrinkViewModel

        fullDrinkViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, fullDrinkViewModel.errorClickListener)
                else hideError()
        })
    }

    private fun showError(@StringRes errorMessage: Int, errorClickListener: View.OnClickListener) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}