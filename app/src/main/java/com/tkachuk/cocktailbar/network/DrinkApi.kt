package com.tkachuk.cocktailbar.network

import com.tkachuk.cocktailbar.model.Drinks
import com.tkachuk.cocktailbar.model.Ingredients
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkApi {

    @GET("list.php?i=list")
    fun getIngredientsList(): Observable<Ingredients>

    @GET("random.php")
    fun getRandom(): Observable<Drinks>

    @GET("search.php")
    fun searchCocktails (@Query("s") str: String): Observable<Drinks>
}