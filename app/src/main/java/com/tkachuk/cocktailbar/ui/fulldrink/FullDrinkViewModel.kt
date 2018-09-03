package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.MutableLiveData
import com.tkachuk.cocktailbar.model.Drink

class FullDrinkViewModel {
    private val drinkName = MutableLiveData<String>()
    private val drinkThumb = MutableLiveData<String>()

    private val strCategory = MutableLiveData<String>()
    private val strAlcoholic = MutableLiveData<String>()
    private val strGlass = MutableLiveData<String>()
    private val strInstructions = MutableLiveData<String>()

    fun bind(drink: Drink) {
        drinkName.value = drink.strDrink
        drinkThumb.value = drink.strDrinkThumb
        strCategory.value = drink.strCategory
        strAlcoholic.value = drink.strAlcoholic
        strGlass.value = drink.strGlass
        strInstructions.value = drink.strInstructions
    }

    fun getDrinkName(): MutableLiveData<String> {
        return drinkName
    }

    fun getDrinkThumb(): MutableLiveData<String> {
        return drinkThumb
    }
}