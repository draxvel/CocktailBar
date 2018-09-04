package com.tkachuk.cocktailbar.ui.fulldrink

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.network.DrinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FullDrinkViewModel: BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private lateinit var subscription: Disposable

    private var id: Int = 0
    private val errorMessage: MutableLiveData<Int> = MutableLiveData()
    private val errorClickListener = View.OnClickListener {loadRecipe(id)}

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
                        { msg -> onRetrieveRecipeError (msg.localizedMessage.toString()) }
                )
    }

    private fun onRetrieveRecipeStart() {
        setVisible(true)
        errorMessage.value = null
    }

    private fun onRetrieveRecipeStop() {
        setVisible(false)
    }

    private fun onRetrieveRecipeSuccess(drinks: List<Drink>) {
        Log.d("draxvel", drinks[0].strDrink)
       bind(drinks[0])
    }

    private fun onRetrieveRecipeError(msg: String) {
        errorMessage.value = R.string.loading_error
        setVisible(false)
        Log.d("draxvel drink", "error + " + msg)
    }
    //TODO binding view + showing data in this fragment

    private fun onSearchDrinkError() {
        errorMessage.value = R.string.not_found
        setVisible(false)
    }

    private fun setVisible(visible: Boolean) {
        if (visible) {
            //loadingVisibility.value = View.VISIBLE
        } else {
            //loadingVisibility.value = View.GONE
        }
    }
}