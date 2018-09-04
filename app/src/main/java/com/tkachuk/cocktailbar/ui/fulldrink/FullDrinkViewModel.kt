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
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private var id: Int = 0
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener {loadRecipe(id)}

    //val title = MutableLiveData<String>()
    val drinkName = MutableLiveData<String>()
    val drinkThumb = MutableLiveData<String>()

    val strCategory = MutableLiveData<String>()
    val strAlcoholic = MutableLiveData<String>()
    val strGlass = MutableLiveData<String>()
    val strInstructions = MutableLiveData<String>()

    fun bind(drink: Drink) {
        drinkName.value = drink.strDrink
        //title.value = drinkName.toString()
        drinkThumb.value = drink.strDrinkThumb
        strCategory.value = drink.strCategory
        strAlcoholic.value = drink.strAlcoholic
        strGlass.value = drink.strGlass
        strInstructions.value = drink.strInstructions
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
        bind(drinks[0])
    }

    private fun onRetrieveRecipeError(msg: String) {
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