package com.tkachuk.cocktailbar.ui.ingredients

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
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

    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadIngredients() }

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
        errorMessage.value = null
        Log.d("draxvel", "start")
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
        Log.d("draxvel", "finish")

    }

    private fun onRetrievePostListSuccess() {
        Log.d("draxvel", "success")
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.loading_error
        Log.d("draxvel", "error")
    }
}