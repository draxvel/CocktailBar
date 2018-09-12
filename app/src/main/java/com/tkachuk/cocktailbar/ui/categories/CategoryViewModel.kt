package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import com.tkachuk.cocktailbar.base.BaseViewModel
import com.tkachuk.cocktailbar.model.Category
import com.tkachuk.cocktailbar.network.DrinkApi
import com.tkachuk.cocktailbar.util.extension.random
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryViewModel: BaseViewModel() {

    @Inject
    lateinit var drinkApi: DrinkApi
    private lateinit var subscription: Disposable

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

    private fun loadPhotoPreviewForCategory(s: String){
        subscription = drinkApi.getFilteredList(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    val random = (0 until result.drinks.size).random()
                    onLoadPhotoPreviewForCategory(result.drinks[random].strDrinkThumb)}
    }

    private fun onLoadPhotoPreviewForCategory(s: String){
        categoryImage.value = s
    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}