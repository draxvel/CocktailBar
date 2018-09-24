package com.tkachuk.cocktailbar.injection.component

import com.tkachuk.cocktailbar.data.repository.CategoryRepository
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface RepositoryModelInjector {

    fun inject(drinkRepository: DrinkRepository)
    fun inject(categoryRepository: CategoryRepository)

    @Component.Builder
    interface Builder {
        fun build(): RepositoryModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}