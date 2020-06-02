package com.stepashka.buildinglocator.dagger

import com.google.gson.Gson
import com.stepashka.buildinglocator.services.LoginServiceSql
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


//first module to provide dependencies



@Module

object NetworkModule {



    private val BASE_URL = LoginServiceSql.BASE_URL



    val gson = Gson()



    val logger = HttpLoggingInterceptor().apply {

        level = HttpLoggingInterceptor.Level.BASIC

        level = HttpLoggingInterceptor.Level.BODY

    }

    private val okHttpClient = OkHttpClient.Builder()

        .addInterceptor(logger)

        .retryOnConnectionFailure(false)

        .readTimeout(10, TimeUnit.SECONDS)

        .connectTimeout(15, TimeUnit.SECONDS)

        .build()



    @Singleton

    @Provides

    @JvmStatic  //generates companion setters and getters for runtime

    fun provideRetrofitInstance(): LoginServiceSql {

        return Retrofit

            .Builder()

            .baseUrl(BASE_URL)

            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create(gson))

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

            .build()

            .create(LoginServiceSql::class.java)



    }





}