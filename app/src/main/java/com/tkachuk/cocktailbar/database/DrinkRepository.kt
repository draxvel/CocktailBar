package com.tkachuk.cocktailbar.database

import android.app.Application
import com.tkachuk.cocktailbar.model.Drink

class DrinkRepository(application: Application) {
    private var drinkDao:DrinkDao
    private var drinkList: List<Drink>

    init{
        val drinkRoomDatabase = DrinkRoomDatabase.getDatabase(application.applicationContext)
        drinkDao = drinkRoomDatabase.drinkDao
        drinkList = drinkDao.allWords
    }

    fun insert(drink: Drink){
        drinkDao.insert(drink)
    }
}