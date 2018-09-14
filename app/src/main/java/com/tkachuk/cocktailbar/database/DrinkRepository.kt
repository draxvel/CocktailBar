package com.tkachuk.cocktailbar.database

import android.app.Application
import android.os.AsyncTask
import com.tkachuk.cocktailbar.model.Drink

class DrinkRepository(application: Application) {
    private var drinkDao:DrinkDao
    var drinkList: List<Drink>

    init{
        val drinkRoomDatabase = DrinkRoomDatabase.getDatabase(application.applicationContext)
        drinkDao = drinkRoomDatabase.drinkDao
        drinkList = drinkDao.allWords
    }

    fun insert(drink: Drink){
        insertTask(drinkDao).execute(drink)
    }

    fun delete(drink: Drink) {
        drinkDao.delete(drink)
    }

    fun isDrinkInDatabase(id: Int):Boolean {
        return drinkDao.isDrinkInDatabase(id) == null
    }

    class insertTask(drinkDao: DrinkDao) : AsyncTask<Drink, Void, Void>() {

        private var dao: DrinkDao = drinkDao

        override fun doInBackground(vararg params: Drink): Void? {
            dao.insert(params[0])
            return null
        }
    }
}