package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.data.repository.CategoryRepository
import com.tkachuk.cocktailbar.ui.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Category
import com.tkachuk.cocktailbar.network.Api
import com.tkachuk.cocktailbar.ui.main.IMainActivity
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CategoryListViewModel(activity: FragmentActivity) : BaseViewModel() {

    @Inject
    lateinit var api: Api

    private val repo = CategoryRepository(activity.applicationContext)
    private var subscription: Disposable? = null
    var categoryListAdapter: CategoryListAdapter = CategoryListAdapter(activity as IMainActivity)

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadCategories() }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        loadingVisibility.value = null
        errorMessage.value = null
    }

    fun loadCategories() {
        onRetrieveCategoriesStart()

        repo.getCategories(loadCategoriesCallBack = object : CallBack.LoadCategoriesCallBack {

            override fun onLoad(list: List<Category>) {
                onRetrieveCategoriesSuccess(list)
                onRetrieveCategoriesFinish()
            }

            override fun onError(msg: String) {
                onRetrieveDrinkError(msg)
                onRetrieveCategoriesFinish()
            }

        })
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

    private fun onRetrieveCategoriesSuccess(list: List<Category>) {
        Log.d("draxvel", "onRetrieveCategoriesSuccess")
        categoryListAdapter.setList(list)
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