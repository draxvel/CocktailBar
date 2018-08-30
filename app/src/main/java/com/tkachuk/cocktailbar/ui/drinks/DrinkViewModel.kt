package com.tkachuk.cocktailbar.ui.drinks

import android.arch.lifecycle.MutableLiveData
import com.tkachuk.cocktailbar.model.Drink

class DrinkViewModel {
    private val drinkName = MutableLiveData<String>()
    private val drinkThumb = MutableLiveData<String>()

    fun bind(drink: Drink) {
        drinkName.value = drink.strDrink
        drinkThumb.value = drink.strDrinkThumb
    }

    fun getDrinkName(): MutableLiveData<String> {
        return drinkName
    }

    fun getDrinkThumb(): MutableLiveData<String> {
        return drinkThumb
    }
}