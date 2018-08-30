package com.tkachuk.cocktailbar.ui.drinks

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.model.Drinks
import com.tkachuk.cocktailbar.network.DrinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrinkListViewModel : BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val drinkListAdapter: DrinkListAdapter = DrinkListAdapter()

    val drinkList: MutableList<Drink> = mutableListOf()

    private lateinit var subscription: Disposable

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRandomDrink() }

    init {
        loadRandomDrink()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadRandomDrink() {
        subscription = drinkApi.getRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDrinkStart() }
                .doOnTerminate { onRetrieveDrinkFinish() }
                .subscribe(
                        //Add result
                        { result -> onRetrieveDrinkSuccess(result) },
                        { msg -> onRetrieveDrinkError(msg.localizedMessage.toString()) }
                )
    }

    fun searchCocktails(str: String){
        subscription = drinkApi.searchCocktails(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> onSearchDrinkSuccess(result)},
                        { msg -> onSearchDrinkError(msg.localizedMessage.toString())}
                )
    }

    private fun onRetrieveDrinkStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
        Log.d("draxvel drink", "start")
    }

    private fun onRetrieveDrinkFinish() {
        loadingVisibility.value = View.GONE
        Log.d("draxvel drink", "finish")
    }

    private fun onRetrieveDrinkSuccess(result: Drinks) {
        Log.d("draxvel drink", "success")
        if (drinkList.size < 15) {
            if (drinkList.contains(result.drinks[0])) {
                loadRandomDrink()
            } else {
                Log.d("draxvel drink = ", result.drinks[0].toString())
                drinkList.add(result.drinks[0])
            }
            loadRandomDrink()
        } else {
            drinkListAdapter.addToList(drinkList)
            Log.d("draxvel drinkLIst = ", drinkList.toString())

            drinkList.clear()
        }
    }

    private fun onRetrieveDrinkError(msg: String) {
        errorMessage.value = R.string.loading_error
        loadingVisibility.value = View.GONE
        Log.d("draxvel drink", "error + " + msg)
    }

    private fun onSearchDrinkSuccess(result: Drinks) {
        Log.d("draxvel search - ", result.toString())
        drinkListAdapter.updateList(result.drinks)
    }

    private fun onSearchDrinkError(msg: String) {

    }
}