package com.tkachuk.cocktailbar.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "ingredient")
data class Ingredient(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "strIngredient1")
        var strIngredient1: String,
        @NonNull
        @ColumnInfo(name = "strMeasure1")
        var strMeasure1: String = "")