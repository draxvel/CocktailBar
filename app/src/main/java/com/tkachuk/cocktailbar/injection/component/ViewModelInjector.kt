package com.tkachuk.cocktailbar.injection.component

import com.tkachuk.cocktailbar.injection.module.NetworkModule
import com.tkachuk.cocktailbar.ui.categories.CategoryListViewModel
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkViewModel
import com.tkachuk.cocktailbar.ui.ingredients.IngredientsListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(ingredientsListViewModel: IngredientsListViewModel)
    fun inject(drinkListViewModel: DrinkListViewModel)
    fun inject(fullDrinkViewModel: FullDrinkViewModel)
    fun inject(categoryListViewModel: CategoryListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}