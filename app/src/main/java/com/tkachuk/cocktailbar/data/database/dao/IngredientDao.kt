package com.tkachuk.cocktailbar.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tkachuk.cocktailbar.model.Ingredient

@Dao
interface IngredientDao {

    //@get:Query("SELECT * from ingredient")
    val allDrinks: List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ingredientList: List<Ingredient>)
}