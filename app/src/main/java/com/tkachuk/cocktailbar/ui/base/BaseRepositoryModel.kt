package com.tkachuk.cocktailbar.ui.base

import com.tkachuk.cocktailbar.data.repository.CategoryRepository
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.injection.component.DaggerRepositoryModelInjector
import com.tkachuk.cocktailbar.injection.component.RepositoryModelInjector
import com.tkachuk.cocktailbar.injection.module.NetworkModule

abstract class BaseRepositoryModel {
    private val injector: RepositoryModelInjector = DaggerRepositoryModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is DrinkRepository -> injector.inject(this)
            is CategoryRepository -> injector.inject(this)
        }
    }
}