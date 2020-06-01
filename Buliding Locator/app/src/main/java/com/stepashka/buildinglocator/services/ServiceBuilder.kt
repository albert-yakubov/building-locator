package com.stepashka.buildinglocator.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceBuilder {

    companion object {






        fun create(): LoginServiceSql {

            val logger = HttpLoggingInterceptor()

            logger.level = HttpLoggingInterceptor.Level.BASIC

            logger.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)

                .retryOnConnectionFailure(true)

                .readTimeout(10, TimeUnit.SECONDS)

                .connectTimeout(15, TimeUnit.SECONDS)

                .build()

            val retrofit = Retrofit.Builder()

                .client(okHttpClient)

                .baseUrl(LoginServiceSql.BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())

                .build()


            return retrofit.create(LoginServiceSql::class.java)

        }

    }



}