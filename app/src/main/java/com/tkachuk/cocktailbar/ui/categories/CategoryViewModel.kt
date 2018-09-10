package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.MutableLiveData
import com.tkachuk.cocktailbar.model.Category

class CategoryViewModel {
    private val categoryName = MutableLiveData<String>()

    fun bind(category: Category) {
        categoryName.value = category.strCategory
    }

    fun getCategoryName(): MutableLiveData<String> {
        return categoryName
    }
}