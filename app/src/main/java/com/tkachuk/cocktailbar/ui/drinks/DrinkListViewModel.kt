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

    val drinkListAdapter: DrinkListAdapter = DrinkListAdapter(this)

    private var drinkList: MutableList<Drink> = mutableListOf()
    var clickedDrinkId: MutableLiveData<Int> = MutableLiveData()

    private var count = 0

    private var subscription: Disposable? = null

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRandom10Drink(false) }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadRandom10Drink(update: Boolean) {
        subscription = drinkApi.getRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDrinkStart() }
                .doOnTerminate { onRetrieveDrinkFinish() }
                .subscribe(
                        //Add result
                        { result ->
                            if (count < 5) {
                                if (drinkList.contains(result.drinks[0])) {
                                    loadRandom10Drink(update)
                                } else {
                                    drinkList.add(result.drinks[0])
                                    count++
                                }
                                loadRandom10Drink(update)
                            } else {
                                onRetrieveDrinkSuccess(update)
                                count = 0
                            }
                        },
                        { msg -> onRetrieveDrinkError(msg.localizedMessage.toString()) }
                )
    }

    fun searchCocktails(str: String) {
        subscription = drinkApi.searchCocktails(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDrinkStart() }
                .doOnTerminate { onRetrieveDrinkFinish() }
                .subscribe(
                        { result -> onSearchDrinkSuccess(result) },
                        { onSearchDrinkError() }
                )
    }

    fun searchByIngredient(str: String) {
        subscription = drinkApi.searchByIngredient(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDrinkStart() }
                .doOnTerminate { onRetrieveDrinkFinish() }
                .subscribe(
                        { result -> onSearchDrinkSuccess(result) },
                        { onSearchDrinkError() }
                )
    }

    private fun onRetrieveDrinkStart() {
        setVisible(true)
        errorMessage.value = null
    }

    private fun onRetrieveDrinkFinish() {
        setVisible(false)
    }

    private fun onRetrieveDrinkSuccess(update: Boolean) {
        if (update) {
            drinkListAdapter.updateList(drinkList)
        } else {
            drinkListAdapter.addToList(drinkList)
        }
        drinkList = mutableListOf()
        count = 0
    }

    private fun onRetrieveDrinkSuccess(result: Drinks, update: Boolean) {
        Log.d("draxvel drink", "success")

        if (drinkList.size < 10) {
            if (drinkList.contains(result.drinks[0])) {
                loadRandom10Drink(update)
            } else {
                drinkList.add(result.drinks[0])
            }
            loadRandom10Drink(update)
        } else {

            if (update) {
                drinkListAdapter.updateList(drinkList)
            } else {
                drinkListAdapter.addToList(drinkList)
            }
            drinkList.clear()
        }
    }

    private fun onRetrieveDrinkError(msg: String) {
        errorMessage.value = R.string.loading_error
        setVisible(false)
        Log.d("draxvel drink", "error + " + msg)
    }

    private fun onSearchDrinkSuccess(result: Drinks) {
        Log.d("draxvel search - ", result.toString())
        drinkListAdapter.updateList(result.drinks)
        drinkList = mutableListOf()
        count = 0
    }

    private fun onSearchDrinkError() {
        errorMessage.value = R.string.not_found
        setVisible(false)
    }

    fun setVisible(visible: Boolean) {
        if (visible) {
            loadingVisibility.value = View.VISIBLE
        } else {
            loadingVisibility.value = View.GONE
        }
    }
}