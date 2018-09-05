package com.tkachuk.cocktailbar.ui.fulldrink.ingredients

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.util.BASE_URL_IMAGES

class IngredientPhotoViewModel: BaseViewModel() {

    val ingredientName = MutableLiveData<String>()
    val ingredientPhoto = MutableLiveData<String>()

    fun bind(ingredient: Ingredient){
        ingredientName.value = ingredient.strIngredient1
        ingredientPhoto.value = "$BASE_URL_IMAGES${ingredientName.value}-Small.png"

        Log.d("draxvel", ingredientPhoto.value.toString())
    }
}