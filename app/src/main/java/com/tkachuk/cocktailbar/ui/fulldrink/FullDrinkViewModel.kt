package com.tkachuk.cocktailbar.ui.fulldrink

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.ui.base.BaseViewModel
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.network.Api
import com.tkachuk.cocktailbar.ui.fulldrink.ingredients.IngredientPhotoListAdapter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FullDrinkViewModel(val activity: FullDrinkActivity) : BaseViewModel() {

    @Inject
    lateinit var api: Api
    private lateinit var subscription: Disposable

    private lateinit var currentDrink: Drink
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val ingredientPhotoListAdapter = IngredientPhotoListAdapter(this)
    var clickedPhotoIngredient: MutableLiveData<Boolean> = MutableLiveData()

    val drinkName = MutableLiveData<String>()
    val drinkThumb = MutableLiveData<String>()

    val strCategory = MutableLiveData<String>()
    val strAlcoholic = MutableLiveData<String>()
    val strGlass = MutableLiveData<String>()
    val strInstructions = MutableLiveData<String>()

    val drinkIsFavorite = MutableLiveData<Boolean>()

    fun bind(drink: Drink) {
        currentDrink = drink
        drinkName.value = drink.strDrink
        drinkThumb.value = drink.strDrinkThumb
        strCategory.value = "Category: " + drink.strCategory
        strAlcoholic.value = "Alcoholic: " + drink.strAlcoholic
        strGlass.value = "Glass: " + drink.strGlass
        strInstructions.value = drink.strInstructions

        val repo = DrinkRepository(activity.applicationContext)
        drinkIsFavorite.value = repo.isDrinkFavorite(currentDrink.idDrink)

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

        ingredientPhotoListAdapter.setList(tempList, false)

        clickedPhotoIngredient.observe(activity, Observer { value ->
            ingredientPhotoListAdapter.setList(tempList, value!!)
        })
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadRecipe(id: Int, application: Application) {
        val repository = DrinkRepository(application)
        onRetrieveRecipeStart()
        repository.getSingleDrinkById(id, loadingSingleDrinkCallBack = object : CallBack.LoadingSingleDrinkCallBack {
            override fun onLoad(drink: Drink) {
                bind(drink)
                onRetrieveRecipeStop()
            }

            override fun onError(msg: String) {
                onRetrieveRecipeError(msg)
                onRetrieveRecipeStop()
            }
        })
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

    private fun onRetrieveRecipeError(msg: String) {
        Log.d("draxvel", "onRetrieveRecipeError")
        errorMessage.value = R.string.loading_error
        setVisible(false)
        Log.d("draxvel", "msg: " + msg)
    }

    private fun setVisible(visible: Boolean) {
        if (visible) {
            loadingVisibility.value = View.VISIBLE
        } else {
            loadingVisibility.value = View.GONE
        }
    }

    fun insertDrink(application: Application) {
        val repo = DrinkRepository(application)
        Log.d("draxvel", "insert: " + currentDrink.strDrink)
        currentDrink.favorite = true
        repo.insert(currentDrink)
    }

    fun deleteDrink(application: Application) {
        val repo = DrinkRepository(application)
        Log.d("draxvel", "delete: " + currentDrink.strDrink)
        repo.deleteFromFavorites(currentDrink.idDrink)
    }
}