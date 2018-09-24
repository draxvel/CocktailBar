package com.tkachuk.cocktailbar.data.database

import com.tkachuk.cocktailbar.model.Drink
import com.tkachuk.cocktailbar.model.Drinks
import io.reactivex.Observable

interface CallBack {

    interface BaseCallback {
        fun onError(msg: String)
    }

    interface LoadingDBCallBack : BaseCallback {
        fun onLoad(list: List<Drink>)
    }

    interface LoadingApiCallBack : BaseCallback {
        fun onLoad(list: List<Drink>)
    }

    interface LoadingMainCallBack : BaseCallback {
        fun onLoad(list: Observable<List<Drink>>)
    }

    interface LoadingSingleDrinkCallBack : BaseCallback {
        fun onLoad(drink: Drink)
    }
}
