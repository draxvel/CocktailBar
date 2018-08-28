package com.tkachuk.cocktailbar.ui.ingredients

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.network.DrinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class IngredientsListViewModel : BaseViewModel() {
    @Inject
    lateinit var drinkApi: DrinkApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private lateinit var subscription: Disposable

    init {
        loadIngredients()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadIngredients() {
        subscription = drinkApi.getIngredientsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { onRetrievePostListSuccess() },
                        { onRetrievePostListError() }
                )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess() {
    }

    private fun onRetrievePostListError() {
    }
}