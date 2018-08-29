package com.tkachuk.cocktailbar.ui.ingredients

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.model.Ingredients
import com.tkachuk.cocktailbar.network.DrinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class IngredientsListViewModel : BaseViewModel() {
    @Inject
    lateinit var drinkApi: DrinkApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val ingredientsListAdapter: IngredientsListAdapter = IngredientsListAdapter()
    val ingredientsListAdapter2: IngredientsListAdapter = IngredientsListAdapter()

    private lateinit var subscription: Disposable

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
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
                        // Add result
                        { result -> onRetrievePostListSuccess(result) },
                        { msg -> onRetrievePostListError(msg.localizedMessage.toString()) }
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

    private fun onRetrievePostListSuccess(result: Ingredients) {
        ingredientsListAdapter.updateList(getRandomIngredients(result))
        val tempList = getRandomIngredients(result, 3).toMutableList()
        tempList.add(Ingredient("SEE MORE"))
        ingredientsListAdapter2.updateList(tempList)
        Log.d("draxvel", "success")
    }

    private fun onRetrievePostListError(msg: String) {
        errorMessage.value = R.string.loading_error
        loadingVisibility.value = View.GONE
        Log.d("draxvel", "error + " + msg)
    }

    private fun getRandomIngredients(result: Ingredients, count: Int = result.drinks.size): List<Ingredient> {
        val mutableTempList = result.drinks.toMutableList()
        mutableTempList.shuffle(Random(System.currentTimeMillis()))
        return mutableTempList.toList().takeLast(count)
    }
}