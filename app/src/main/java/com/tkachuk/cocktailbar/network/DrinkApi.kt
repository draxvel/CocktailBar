package com.tkachuk.cocktailbar.network

import android.database.Observable
import com.tkachuk.cocktailbar.model.Drink
import retrofit2.http.GET

interface DrinkApi {

    @GET("/list.php?i=list")
    fun getIngredientsList(): Observable<Drink>
}