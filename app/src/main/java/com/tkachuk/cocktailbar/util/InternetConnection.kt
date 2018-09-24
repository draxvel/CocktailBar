package com.tkachuk.cocktailbar.util

import android.content.Context
import android.net.ConnectivityManager

fun isNetworkConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetworkInfo != null
}