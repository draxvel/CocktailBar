package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.network.DrinkApi
import com.tkachuk.cocktailbar.ui.fulldrink.ingredients.IngredientPhotoListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FullDrinkViewModel: BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private var id: Int = 0
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val ingredientPhotoListAdapter = IngredientPhotoListAdapter()

    val drinkName = MutableLiveData<String>()
    val drinkThumb = MutableLiveData<String>()

    val strCategory = MutableLiveData<String>()
    val strAlcoholic = MutableLiveData<String>()
    val strGlass = MutableLiveData<String>()
    val strInstructions = MutableLiveData<String>()

    fun bind(drink: Drink) {
        drinkName.value = drink.strDrink
        drinkThumb.value = drink.strDrinkThumb
        strCategory.value = drink.strCategory
        strAlcoholic.value = drink.strAlcoholic
        strGlass.value = drink.strGlass
        strInstructions.value = drink.strInstructions

        val tempList: MutableList<Ingredient> = mutableListOf()

        if(drink.strIngredient1!=null && drink.strIngredient1.isNotEmpty()) tempList.add(Ingredient(drink.strIngredient1))
        if(drink.strIngredient2!=null && drink.strIngredient2.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient2))
        if(drink.strIngredient3!=null && drink.strIngredient3.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient3))
        if(drink.strIngredient4!=null && drink.strIngredient4.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient4))
        if(drink.strIngredient5!=null && drink.strIngredient5.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient5))
        if(drink.strIngredient6!=null && drink.strIngredient6.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient6))
        if(drink.strIngredient7!=null && drink.strIngredient7.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient7))
        if(drink.strIngredient8!=null && drink.strIngredient8.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient8))
        if(drink.strIngredient9!=null && drink.strIngredient9.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient9))
        if(drink.strIngredient10!=null && drink.strIngredient10.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient10))
        if(drink.strIngredient11!=null && drink.strIngredient11.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient11))
        if(drink.strIngredient12!=null && drink.strIngredient12.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient12))
        if(drink.strIngredient13!=null && drink.strIngredient13.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient13))
        if(drink.strIngredient14!=null && drink.strIngredient14.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient14))
        if(drink.strIngredient15!=null && drink.strIngredient15.isNotEmpty())  tempList.add(Ingredient(drink.strIngredient15))

        ingredientPhotoListAdapter.setList(tempList)
        Log.d("draxvel", "list - "+tempList.toString())
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadRecipe(id: Int) {
        Log.d("draxvel", "loadRecipe"+id)

        subscription = drinkApi.getFullCocktailRecipe(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveRecipeStart() }
                .doOnTerminate { onRetrieveRecipeStop() }
                .subscribe(
                        //Add result
                        { result ->
                            onRetrieveRecipeSuccess (result.drinks)
                        },
                        { msg -> onRetrieveRecipeError () }
                )
    }

    private fun onRetrieveRecipeStart() {
        Log.d("draxvel", "onRetrieveRecipeStart")
        setVisible(true)
        errorMessage.value = null
    }

    private fun onRetrieveRecipeStop() {
        Log.d("draxvel", "onRetrieveRecipeStop")
        setVisible(false)
    }

    private fun onRetrieveRecipeSuccess(drinks: List<Drink>) {
        Log.d("draxvel", "onRetrieveRecipeSuccess")
        bind(drinks[0])
    }

    private fun onRetrieveRecipeError() {
        Log.d("draxvel", "onRetrieveRecipeError")
        errorMessage.value = R.string.loading_error
        setVisible(false)
    }

    private fun setVisible(visible: Boolean) {
        if (visible) {
            loadingVisibility.value = View.VISIBLE
        } else {
            loadingVisibility.value = View.GONE
        }
    }
}