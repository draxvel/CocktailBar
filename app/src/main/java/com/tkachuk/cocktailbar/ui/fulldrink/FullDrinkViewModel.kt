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

class FullDrinkViewModel : BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

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
        strCategory.value = "Category: " + drink.strCategory
        strAlcoholic.value = "Alcoholic: " + drink.strAlcoholic
        strGlass.value = "Glass: " + drink.strGlass
        strInstructions.value = drink.strInstructions

        val tempList: MutableList<Ingredient> = mutableListOf()

        val fields = drink.javaClass.declaredFields

        var count = 0
        for (f in fields) {
            f.isAccessible = true
            if (f.name.startsWith("strIngredient") && f.get(drink) != null) {
                val str: String = f.get(drink) as String
                if (str != "") {
                    val ingredient = Ingredient(str)
                    tempList.add(ingredient)
                    count++
                }
            }
        }

        var i = 0
        for (f in fields) {
            if (f.name.startsWith("strMeasure") && f.get(drink) != null) {
                val str: String = f.get(drink) as String
                if (str != "" && str != " " && i < count) {
                    tempList[i].strMeasure1 = str
                    i++
                }
            }
        }

        ingredientPhotoListAdapter.setList(tempList)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadRecipe(id: Int) {
        subscription = drinkApi.getFullCocktailRecipe(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveRecipeStart() }
                .doOnTerminate { onRetrieveRecipeStop() }
                .subscribe(
                        //Add result
                        { result ->
                            onRetrieveRecipeSuccess(result.drinks)
                        },
                        { msg -> onRetrieveRecipeError(msg) }
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

    private fun onRetrieveRecipeError(msg: Throwable) {
        Log.d("draxvel", "onRetrieveRecipeError")
        errorMessage.value = R.string.loading_error
        setVisible(false)
        Log.d("draxvel", "msg: " + msg.localizedMessage.toString())
    }

    private fun setVisible(visible: Boolean) {
        if (visible) {
            loadingVisibility.value = View.VISIBLE
        } else {
            loadingVisibility.value = View.GONE
        }
    }
}