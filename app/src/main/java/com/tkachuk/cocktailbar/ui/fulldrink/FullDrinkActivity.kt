package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tkachuk.cocktailbar.R
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tkachuk.cocktailbar.databinding.ActivityFullDrinkBinding

class FullDrinkActivity: AppCompatActivity() {

    val binding: ActivityFullDrinkBinding by lazy{
        DataBindingUtil.setContentView<ActivityFullDrinkBinding>(this, R.layout.activity_full_drink)
    }
    private lateinit var fullDrinkViewModel: FullDrinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("draxvel", "onCreate")

        fullDrinkViewModel = ViewModelProviders.of(this).get(FullDrinkViewModel::class.java)

        val id = intent.getIntExtra("id", 1)

        fullDrinkViewModel.loadRecipe(id)
        binding.fullDrinkViewModel = fullDrinkViewModel
    }
}