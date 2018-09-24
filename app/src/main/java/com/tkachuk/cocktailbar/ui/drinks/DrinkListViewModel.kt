package com.tkachuk.cocktailbar.ui.drinks

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.ui.base.BaseViewModel
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.model.Drink
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class DrinkListViewModel(private val drinkRepository: DrinkRepository) : BaseViewModel() {

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val drinkListAdapter: DrinkListAdapter = DrinkListAdapter(this)

    var clickedDrinkId: MutableLiveData<Int> = MutableLiveData()

    private var subscription: Disposable? = null

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRandomDrinks(false) }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        drinkListAdapter.clear()
        loadingVisibility.value = null
        errorMessage.value = null
    }

    //Main Fragment
    fun loadRandomDrinks(update: Boolean) {
        onRetrieveStart()
        drinkRepository.getDrinks(loadingMainCallBack = object : CallBack.LoadingMainCallBack {
            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    onRetrieveDrinkSuccess(it, update)
                    onRetrieveFinish()
                }
            }

            override fun onError(msg: String) {
                onRetrieveDrinkError()
            }
        })
    }

    //Main Fragment
    fun searchCocktails(str: String) {
        onRetrieveStart()
        drinkRepository.searchCocktails(str, loadingMainCallBack = object : CallBack.LoadingMainCallBack{
            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    onRetrieveDrinkSuccess(it, true)
                    onRetrieveFinish()
                }
            }
            override fun onError(msg: String) {
                onSearchDrinkError()
            }
        })
    }

    //MainFragment
    fun searchByIngredient(str: String) {
        onRetrieveStart()
        drinkRepository.searchByIngredient(str, loadingMainCallBack = object : CallBack.LoadingMainCallBack{

            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    onRetrieveDrinkSuccess(it, true)
                    onRetrieveFinish()
                }
            }

            override fun onError(msg: String) {
                onSearchDrinkError()            }

        })
    }

    fun loadFilteredDrinks(str: String) {
        onRetrieveStart()
        drinkRepository.loadFilteredDrinks(str, loadingMainCallBack = object : CallBack.LoadingMainCallBack{

            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    onRetrieveDrinkSuccess(it, true)
                    onRetrieveFinish()
                }
            }

            override fun onError(msg: String) {
                onSearchDrinkError()
            }
        })
    }

    fun loadFavorites(): Boolean {
        var isSuccess = true
        drinkRepository.loadFavorites(loadingMainCallBack = object : CallBack.LoadingMainCallBack{

            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    onRetrieveDrinkSuccess(it, true)
                    isSuccess = true
                }
            }

            override fun onError(msg: String) {
                isSuccess = true
            }
        })

        return isSuccess
    }

    private fun onRetrieveStart() {
        setVisible(true)
        errorMessage.value = null
    }

    private fun onRetrieveFinish() {
        setVisible(false)
    }

    private fun onRetrieveDrinkSuccess(drinks: List<Drink>, update: Boolean) {
        if (update) {
            drinkListAdapter.updateList(drinks.toMutableList())
        } else {
            drinkListAdapter.addToList(drinks.toMutableList())
        }
    }

    private fun onRetrieveDrinkError() {
        errorMessage.value = R.string.loading_error
        setVisible(false)
    }

    private fun onSearchDrinkError() {
        errorMessage.value = R.string.not_found
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