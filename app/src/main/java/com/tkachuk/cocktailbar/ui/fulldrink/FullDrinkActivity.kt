package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tkachuk.cocktailbar.R
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tkachuk.cocktailbar.databinding.ActivityFullDrinkBinding

class FullDrinkActivity : AppCompatActivity() {

    val binding: ActivityFullDrinkBinding by lazy {
        DataBindingUtil.setContentView<ActivityFullDrinkBinding>(this, R.layout.activity_full_drink)
    }

    private lateinit var fullDrinkViewModel: FullDrinkViewModel
    private var errorSnackBar: Snackbar? = null
    private lateinit var toolbar: Toolbar

    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar = binding.fullDrinkToolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        fullDrinkViewModel = FullDrinkViewModel(this)

        val id = intent.getIntExtra("id", 1)

        fullDrinkViewModel.loadRecipe(id, this.application)
        binding.fullDrinkViewModel = fullDrinkViewModel

        fullDrinkViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage)
            else hideError()
        })

        fullDrinkViewModel.drinkName.observe(this, Observer { title ->
            supportActionBar?.title = title
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        fullDrinkViewModel.drinkIsFavorite.observe(this, Observer { isFavorite->
            if(isFavorite!!){
                menuItem?.icon = ContextCompat.getDrawable(this, R.mipmap.ic_fav)

            }else {
                menuItem?.icon =  ContextCompat.getDrawable(this, R.mipmap.ic_fav_border)
            }
        })
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fulldrink, menu)
        menuItem = menu?.findItem(R.id.favorite_item)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.favorite_item){
            Log.d("draxvel", "onclick")

            if(fullDrinkViewModel.drinkIsFavorite.value==false)
            {
                Log.d("draxvel", "icon has border, replace!")
                fullDrinkViewModel.insertDrink(this.application)
                fullDrinkViewModel.drinkIsFavorite.value = true
            }else{
                fullDrinkViewModel.deleteDrink(this.application)
                Log.d("draxvel", "icon fav, replace!")
                fullDrinkViewModel.drinkIsFavorite.value = false
            }
        }
        return true
    }
}