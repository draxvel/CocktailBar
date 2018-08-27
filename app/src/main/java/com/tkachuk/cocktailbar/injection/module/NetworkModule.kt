package com.tkachuk.cocktailbar.injection.module

import com.tkachuk.cocktailbar.network.DrinkApi
import com.tkachuk.cocktailbar.util.BASE_URL
import dagger.Module
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkModule {

    internal fun provideDrinkApi(retrofit: Retrofit): DrinkApi {
        return retrofit.create(DrinkApi::class.java)
    }

    internal fun provideRetrofitInterface(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }
}