package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Categories
import com.tkachuk.cocktailbar.network.DrinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryListViewModel: BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private var subscription: Disposable? = null
    val categoryListAdapter: CategoryListAdapter = CategoryListAdapter()

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener {loadCategories()}

    init {
        loadCategories()
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadCategories(){
        subscription = drinkApi.getCategoriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveCategoriesStart() }
                .doOnTerminate { onRetrieveCategoriesFinish() }
                .subscribe(
                        //Add result
                        { result -> onRetrieveCategoriesSuccess(result) },
                        { onRetrieveDrinkError() }
                )
    }

    private fun onRetrieveCategoriesStart() {
        errorMessage.value = null
        setVisible(true)
    }

    private fun onRetrieveCategoriesFinish() {
        setVisible(false)
    }

    private fun onRetrieveCategoriesSuccess(result: Categories) {
        categoryListAdapter.setList(result.categories)
    }

    private fun onRetrieveDrinkError() {
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