package com.tkachuk.cocktailbar.ui.ingredients

import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.network.DrinkApi
import javax.inject.Inject

class IngredientsListViewModel: BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
}