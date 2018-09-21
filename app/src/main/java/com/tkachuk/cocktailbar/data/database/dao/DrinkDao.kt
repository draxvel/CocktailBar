package com.tkachuk.cocktailbar.data.database.dao

import android.arch.persistence.room.*
import com.tkachuk.cocktailbar.model.Drink
import io.reactivex.Single

@Dao
interface DrinkDao {

    @Query("SELECT * from drink")
    fun getDrinks(): Single<List<Drink>>

    @Query("SELECT * from drink where strDrink LIKE :str")
    fun searchCocktails(str: String): Single<List<Drink>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(drink: Drink)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(drink: List<Drink>)

    @Delete
    fun delete(drink: Drink)

    @Query("SELECT * from drink where idDrink = :id")
    fun isDrinkInDatabase(id: Int): Drink?
}