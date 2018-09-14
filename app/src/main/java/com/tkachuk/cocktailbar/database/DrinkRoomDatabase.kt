package com.tkachuk.cocktailbar.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tkachuk.cocktailbar.model.Drink

@Database(entities = [Drink::class], version = 1)
abstract class DrinkRoomDatabase: RoomDatabase() {
    abstract val drinkDao: DrinkDao

    companion object {

        private var instance: DrinkRoomDatabase? = null

        fun getDatabase(context: Context): DrinkRoomDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                        DrinkRoomDatabase::class.java, "drinks")
                        .allowMainThreadQueries()
                        .build()
            }
            return instance as DrinkRoomDatabase
        }
    }
}