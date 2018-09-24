package com.tkachuk.cocktailbar.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tkachuk.cocktailbar.data.database.dao.DrinkDao
import com.tkachuk.cocktailbar.model.Category
import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.model.Ingredient

//Category::class, Ingredient::class]
@Database(entities = [Drink::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val drinkDao: DrinkDao
    //abstract val categoryDao: CategoryDao
    //abstract val ingredientDao: IngredientDao

    companion object {

        private var instance: RoomDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance as AppDatabase
        }
    }
}