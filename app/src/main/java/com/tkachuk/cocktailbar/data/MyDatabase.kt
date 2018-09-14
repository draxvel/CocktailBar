package com.tkachuk.cocktailbar.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tkachuk.cocktailbar.model.Drink

@Database(entities = [(Drink::class)], version = 1)
abstract class Database: RoomDatabase() {

    public abstract fun drinkDao(): DrinkDao
}