package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Categories
import com.tkachuk.cocktailbar.network.DrinkApi
import com.tkachuk.cocktailbar.ui.main.IMainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryListViewModel(activity: FragmentActivity) : BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private var subscription: Disposable? = null
    var categoryListAdapter: CategoryListAdapter = CategoryListAdapter(activity as IMainActivity)

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadCategories() }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadCategories() {
        subscription = drinkApi.getCategoriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveCategoriesStart() }
                .doOnTerminate { onRetrieveCategoriesFinish() }
                .subscribe(
                        //Add result
                        { result -> onRetrieveCategoriesSuccess(result) },
                        { msg -> onRetrieveDrinkError(msg.localizedMessage.toString()) }
                )
    }

    private fun onRetrieveCategoriesStart() {
        Log.d("draxvel", "onRetrieveCategoriesStart")

        errorMessage.value = null
        setVisible(true)
    }

    private fun onRetrieveCategoriesFinish() {
        Log.d("draxvel", "onRetrieveCategoriesFinish")

        setVisible(false)
    }

    private fun onRetrieveCategoriesSuccess(result: Categories) {
        Log.d("draxvel", "onRetrieveCategoriesSuccess")
        categoryListAdapter.setList(result.drinks)
    }

    private fun onRetrieveDrinkError(msg: String) {
        Log.d("draxvel msg = ", msg)
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