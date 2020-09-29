package com.stepashka.buildinglocator2.loginMVVMnetwork

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginAPI {

    @FormUrlEncoded
    @POST("login?grant_type=password")
    fun loginMVVM(@Header("Authorization") authorization: String, @Header("Content-Type") content_type: String,
              @Field("username") username: String, @Field("password") password: String) : Call<ResponseBody>

    companion object{
        operator fun invoke() : LoginAPI{
            return Retrofit.Builder().baseUrl("https://ay-my-location.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginAPI::class.java)
        }

    }
}