package com.tkachuk.cocktailbar.model

import android.arch.persistence.room.Entity

//@Entity(tableName = "ingredient")
data class Ingredient(var strIngredient1: String, var strMeasure1: String = "")