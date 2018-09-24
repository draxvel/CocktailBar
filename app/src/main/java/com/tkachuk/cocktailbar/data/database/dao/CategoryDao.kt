package com.tkachuk.cocktailbar.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tkachuk.cocktailbar.model.Category

@Dao
interface CategoryDao{

    //@get:Query("SELECT * from category")
    val allCategories: List<Category>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoryList: List<Category>)
}