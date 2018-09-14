package com.tkachuk.cocktailbar.database

import android.arch.persistence.room.*
import com.tkachuk.cocktailbar.model.Drink

@Dao
interface DrinkDao {

    @get:Query("SELECT * from drinks")
    val allWords: List<Drink>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(drink: Drink)

    @Delete
    fun delete(drink: Drink)

    @Query("SELECT * from drinks where idDrink = :id")
    fun isDrinkInDatabase(id: Int): Drink?
}