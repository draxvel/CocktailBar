package com.tkachuk.cocktailbar.ui.base

import android.arch.lifecycle.ViewModel
import com.tkachuk.cocktailbar.injection.component.DaggerViewModelInjector
import com.tkachuk.cocktailbar.injection.component.ViewModelInjector
import com.tkachuk.cocktailbar.injection.module.NetworkModule
import com.tkachuk.cocktailbar.ui.categories.CategoryListViewModel
import com.tkachuk.cocktailbar.ui.categories.CategoryViewModel
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkViewModel
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is IngredientsListViewModel -> injector.inject(this)
            is DrinkListViewModel -> injector.inject(this)
            is FullDrinkViewModel -> injector.inject(this)
            is CategoryListViewModel -> injector.inject(this)
            is CategoryViewModel -> injector.inject(this)
        }
    }
}