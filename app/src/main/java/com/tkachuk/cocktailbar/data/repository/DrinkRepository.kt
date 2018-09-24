package com.tkachuk.cocktailbar.data.repository

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.tkachuk.cocktailbar.data.database.dao.DrinkDao
import com.tkachuk.cocktailbar.data.database.AppDatabase
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.network.Api
import com.tkachuk.cocktailbar.ui.base.BaseRepositoryModel
import com.tkachuk.cocktailbar.util.COUNT_OF_DRINKS_FOR_GETTING_RANDOM
import com.tkachuk.cocktailbar.util.TIMEOUT_FOR_LOADING
import com.tkachuk.cocktailbar.util.extension.random
import com.tkachuk.cocktailbar.util.isNetworkConnected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DrinkRepository(val context: Context) : BaseRepositoryModel() {

    @Inject
    lateinit var api: Api

    private val drinkDao = AppDatabase.getDatabase(context).drinkDao

    private var counter = 0
    private val tempDrinkList: MutableList<Drink> = mutableListOf()
    private var main: CallBack.LoadingMainCallBack? = null

    fun getDrinks(loadingMainCallBack: CallBack.LoadingMainCallBack) {

        main = loadingMainCallBack
        counter = 0

        if (!isNetworkConnected(context)) {

            getDrinksFromBD(loadingDBCallBack = object : CallBack.LoadingDBCallBack {
                override fun onLoad(list: List<Drink>) {
                    main?.onLoad(Observable.fromArray(list))
                }

                override fun onError(msg: String) {
                    main?.onError(msg)
                }
            })
        } else {

            getDrinksFromApi(loadingApiCallBack = object : CallBack.LoadingApiCallBack {
                override fun onLoad(list: List<Drink>) {
                    loadingMainCallBack.onLoad(Observable.fromArray(list))
                }

                override fun onError(msg: String) {
                    loadingMainCallBack.onError(msg)
                }
            })
        }
    }

    fun searchCocktails(str: String, loadingMainCallBack: CallBack.LoadingMainCallBack) {
        if (!isNetworkConnected(context)) {
            drinkDao.searchCocktails("%$str%")
                    .filter { it.isNotEmpty() }
                    .toObservable()
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        } else {
            api.searchCocktails(str)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result.drinks)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        }
    }

    fun searchByIngredient(str: String, loadingMainCallBack: CallBack.LoadingMainCallBack) {
        if (!isNetworkConnected(context)) {
            drinkDao.searchByIngredient(str)
                    .filter { it.isNotEmpty() }
                    .toObservable()
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        } else {
            api.searchByIngredient(str)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result.drinks)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        }
    }

    fun loadFilteredDrinks(str: String, loadingMainCallBack: CallBack.LoadingMainCallBack) {
        if (!isNetworkConnected(context)) {
            drinkDao.getFilteredList(str)
                    .filter { it.isNotEmpty() }
                    .toObservable()
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        } else {
            api.getFilteredList(str)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingMainCallBack.onLoad(Observable.fromArray(result.drinks)) },
                            { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                    )
        }
    }

    fun loadFavorites(loadingMainCallBack: CallBack.LoadingMainCallBack) {
        drinkDao.loadFavorites()
                .filter { it.isNotEmpty() }
                .toObservable()
                .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                .subscribe(
                        { result -> loadingMainCallBack.onLoad(Observable.fromArray(result)) },
                        { msg -> loadingMainCallBack.onError(msg.localizedMessage ?: "Error") }
                )
    }

    fun getSingleDrinkById(id: Int, loadingSingleDrinkCallBack: CallBack.LoadingSingleDrinkCallBack) {
        if (!isNetworkConnected(context)) {
            drinkDao.getSingleDrinkById(id)
                    .toObservable()
                    .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                    .subscribe(
                            { result -> loadingSingleDrinkCallBack.onLoad(result!!) },
                            { msg ->
                                loadingSingleDrinkCallBack.onError(msg.localizedMessage ?: "Error")
                            }
                    )
        } else {
            api.getFullCocktailRecipe(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result -> loadingSingleDrinkCallBack.onLoad(result.drinks[0]) },
                            { msg ->
                                loadingSingleDrinkCallBack.onError(msg.localizedMessage ?: "Error")
                            }
                    )
        }
    }

    private fun getDrinksFromBD(loadingDBCallBack: CallBack.LoadingDBCallBack) {
        drinkDao.getDrinks()
                .filter { it.isNotEmpty() }
                .toObservable()
                .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                .subscribe({ result ->

                    while (counter< COUNT_OF_DRINKS_FOR_GETTING_RANDOM && COUNT_OF_DRINKS_FOR_GETTING_RANDOM<result.size){

                        val random = (0 until result.size).random()

                        if (!tempDrinkList.contains(result[random])) {
                            tempDrinkList.add(result[random])
                            counter++
                        }
                    }

                    loadingDBCallBack.onLoad(tempDrinkList)
                    counter = 0
                    tempDrinkList.clear()
                },
                        { e ->
                            loadingDBCallBack.onError(e.localizedMessage ?: "Error")
                            counter = 0
                            tempDrinkList.clear()
                        })
    }

    private fun getDrinksFromApi(loadingApiCallBack: CallBack.LoadingApiCallBack) {
        api.getRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(TIMEOUT_FOR_LOADING, TimeUnit.SECONDS)
                .subscribe({ result ->
                    if (counter < COUNT_OF_DRINKS_FOR_GETTING_RANDOM) {
                        if (tempDrinkList.contains(result.drinks[0])) {
                            getDrinksFromApi(loadingApiCallBack)
                        } else {
                            tempDrinkList.add(result.drinks[0])
                            counter++
                        }
                        getDrinksFromApi(loadingApiCallBack)
                    } else {
                        storeDrinksInBD(tempDrinkList)
                        loadingApiCallBack.onLoad(tempDrinkList)
                        counter = 0
                        tempDrinkList.clear()
                    }
                },
                        { e ->
                            loadingApiCallBack.onError(e.localizedMessage ?: "Error")
                            counter = 0
                            tempDrinkList.clear()
                        })
    }

    private fun storeDrinksInBD(drinks: List<Drink>) {
        Observable.fromCallable { drinkDao.insertAll(drinks) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    Log.d("draxvel", "Inserted ${drinks.size} drinks from API in DB...")
                    tempDrinkList.clear()
                }, {})
    }

    fun insert(drink: Drink) {
        InsertTask(drinkDao).execute(drink)
    }

    fun delete(drink: Drink) {
        drinkDao.delete(drink)
    }

    fun isDrinkFavorite(id: Int): Boolean {
        return drinkDao.getDrinkById(id) != null && drinkDao.getDrinkById(id)?.favorite == true
    }

    class InsertTask(drinkDao: DrinkDao) : AsyncTask<Drink, Void, Void>() {

        private var dao: DrinkDao = drinkDao

        override fun doInBackground(vararg params: Drink): Void? {
            dao.insert(params[0])
            return null
        }
    }
}