package com.stepashka.buildinglocator2.services

import com.stepashka.buildinglocator2.models.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginServiceSql{


    @GET("postedmaps/postedmaps")
    //fun getAllProperties(@Header("Authorization") authToken: String): Call<Properties>
    fun getAllMaps() : Call<MutableList<PostedMaps>>



    @GET("users/users")
    //fun getAllUsers(@Header("Authorization") authToken: String): Call<Properties>
    fun getAllUsers() : Call<MutableList<User>>

    @GET("users/user/{userId}")
    fun getUserById(@Path("userId") userId: Long) : Call<User>

    @PUT("users/user/{id}")
    fun updateUserById(@Path("id") id: Long, @Body updateUser: UpdateUser) : Call<Void>

    @PUT("users/user/{id}")
    fun updateUserPById(@Path("id") id: Long, @Body resetPassword: ResetPassword) : Call<Void>

    @POST("users/user")
    fun createUser(@Body newUser: Neweruser): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login?grant_type=password&client_id=lambdaclient&client_secret=lambdasecret")
    fun login(@Header ("Authorization") authorization: String, @Header("Content-Type") content_type: String,
              @Field("username") username: String, @Field("password") password: String) : Call<ResponseBody>

    @POST("postedmaps/postedmap")
    //  fun createProperty(@Header("Authorization") authToken: String, @Body newProperty: NewProperty): Call<Void>
    fun createNewMap(@Body newMap: NewMap) : Call<Void>

    @GET("users/user/name/{userName}")

    fun getUser(@Path("userName")username: String): Call<UserResult>

    @DELETE("postedmaps/postedmap/{id}")
    fun deleteMap(@Path("id") id: Long) : Call<Void>




    @GET("users/user/name/{userName}")
    fun getUser2(@Path("userName")username: String): Call<User>

    @GET("users/user/name/{userName}")
    fun getUser3(@Path("userName")username: String): Call<User>


    @GET("postedmaps/postedmaps/title/like/{title}")
    fun getTitle(@Path("title") title: String): Observable<MutableList<PostedMaps>>


    @GET("postedmaps/postedmaps/address/like/{address}")
    fun getAddress(@Path("address") address: String): Observable<MutableList<PostedMaps>>

    @GET("postedmaps/postedmaps/{id}")
    fun getById(@Path("id") id: Long): Call<PostedMaps>

    companion object {

        const val BASE_URL = "https://ay-my-location.herokuapp.com/"

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .baseUrl(BASE_URL)
            .build()


    }

}