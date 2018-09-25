package com.tkachuk.cocktailbar.ui.ingredients

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.data.repository.IngredientRepository
import com.tkachuk.cocktailbar.ui.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Ingredient
import com.tkachuk.cocktailbar.network.Api
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class IngredientsListViewModel(private val ingredientRepository: IngredientRepository) : BaseViewModel() {
    @Inject
    lateinit var api: Api

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val ingredientsListAdapter: IngredientsListAdapter = IngredientsListAdapter(this)

    private var ingredientsList: List<Ingredient> = listOf()

    var clickedIngredientName: MutableLiveData<String> = MutableLiveData()

    private var subscription: Disposable? = null

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadIngredients(true) }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadIngredients(only5elements: Boolean) {

        onRetrieveIngredientsListStart()

        ingredientRepository.loadIngredients(loadIngredientsCallBack = object : CallBack.LoadIngredientsCallBack {

            override fun onLoad(list: List<Ingredient>) {

                Log.d("draxvel", "loadIngredients onLoad ${list.toString()}")

                if (only5elements) {
                    val tempList = getRandomIngredients(list, 5).toMutableList()
                    tempList.add(Ingredient("SEARCH MORE"))
                    onRetrieveIngredientsListSuccess(tempList.toMutableList())
                } else onRetrieveIngredientsListSuccess(list.toMutableList())

                onRetrieveIngredientsListFinish()
            }

            override fun onError(msg: String) {
                Log.d("draxvel", msg)
                onRetrieveIngredientsListError()
                onRetrieveIngredientsListFinish()
            }

        })
    }

    private fun onRetrieveIngredientsListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveIngredientsListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveIngredientsListSuccess(list: MutableList<Ingredient>) {
        Log.d("draxvel", "onRetrieveIngredientsListSuccess - " + list.toString())
        val sortedList = list.sortedBy { it.strIngredient1 }
        updateAdapter(sortedList)
        ingredientsList = sortedList
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveIngredientsListError() {
        errorMessage.value = R.string.loading_error
        loadingVisibility.value = View.GONE
    }

    private fun getRandomIngredients(list: List<Ingredient>, count: Int = list.size): List<Ingredient> {
        val mutableTempList = list.toMutableList()
        mutableTempList.shuffle(Random(System.currentTimeMillis()))
        return mutableTempList.toList().takeLast(count)
    }

    private fun updateAdapter(list: List<Ingredient>) {
        Log.d("draxvel", list.toString())
        ingredientsListAdapter.updateList(list)
    }

    fun search(query: String) {

        val filteredOutPut: MutableList<Ingredient> = mutableListOf()

        if (ingredientsList.isNotEmpty()) {
            for (item in ingredientsList) {
                if (item.strIngredient1.toLowerCase().startsWith(query.toLowerCase())) {
                    filteredOutPut.add(item)
                }
            }
            if (filteredOutPut.isNotEmpty()) {
                updateAdapter(filteredOutPut)
            }
        }
    }
}