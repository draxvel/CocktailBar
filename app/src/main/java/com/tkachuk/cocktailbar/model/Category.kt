package com.tkachuk.cocktailbar.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "category")
data class Category(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "strCategory")
        val strCategory: String)