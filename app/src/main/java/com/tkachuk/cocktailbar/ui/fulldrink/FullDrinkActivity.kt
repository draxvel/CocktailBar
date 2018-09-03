package com.tkachuk.cocktailbar.ui.fulldrink

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.ActivityMainBinding
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

class FullDrinkActivity: AppCompatActivity() {

    private lateinit var fullDrinkViewModel: FullDrinkViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_item)

        var mView = binding.root
    }

    //TODO binding view + showing data in this fragment
}