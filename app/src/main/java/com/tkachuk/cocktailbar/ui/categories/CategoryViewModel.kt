package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.ui.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Category
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.network.Api
import com.tkachuk.cocktailbar.util.extension.random
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CategoryViewModel(context: Context) : BaseViewModel() {

    @Inject
    lateinit var api: Api
    private lateinit var subscription: Disposable
    private val repo = DrinkRepository(context)

    private val categoryName = MutableLiveData<String>()
    private val categoryImage = MutableLiveData<String>()

    fun bind(category: Category) {
        categoryName.value = category.strCategory
        loadPhotoPreviewForCategory(category.strCategory)
    }

    fun getCategoryName(): MutableLiveData<String> {
        return categoryName
    }

    fun getCategoryImage(): MutableLiveData<String> {
        return categoryImage
    }

    private fun loadPhotoPreviewForCategory(s: String) {
        repo.loadFilteredDrinks(s, loadingMainCallBack = object : CallBack.LoadingMainCallBack {

            override fun onLoad(list: Observable<List<Drink>>) {
                list.subscribe {
                    val random = (0 until it.size).random()
                    onLoadPhotoPreviewForCategory(it[random].strDrinkThumb)
                }
            }

            override fun onError(msg: String) {
            }
        })
    }

    private fun onLoadPhotoPreviewForCategory(s: String) {
        categoryImage.value = s
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}