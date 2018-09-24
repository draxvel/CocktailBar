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

    @Query("SELECT * from drink where strIngredient1 == :str or strIngredient2 == :str or strIngredient3 == :str " +
            "or strIngredient4 == :str or strIngredient5 == :str or strIngredient6 == :str or strIngredient7 == :str " +
            "or strIngredient8 == :str or strIngredient8 == :str or strIngredient9 == :str or strIngredient10 == :str ")
    fun searchByIngredient(str: String): Single<List<Drink>>

    @Query("SELECT * from drink where strCategory == :str")
    fun getFilteredList(str: String): Single<List<Drink>>

    @Query("SELECT * from drink where favorite == 1")
    fun loadFavorites(): Single<List<Drink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(drink: List<Drink>)

    @Delete
    fun delete(drink: Drink)

    @Query("SELECT * from drink where idDrink = :id")
    fun getDrinkById(id: Int): Drink?

    @Query("SELECT * from drink where idDrink = :id")
    fun getSingleDrinkById(id: Int): Single<Drink?>
}