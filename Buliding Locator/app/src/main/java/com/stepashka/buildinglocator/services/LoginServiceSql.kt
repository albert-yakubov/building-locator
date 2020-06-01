package com.stepashka.buildinglocator.services

import com.stepashka.buildinglocator.models.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginServiceSql{


//    @GET("campaigns/campaigns")
//    //fun getAllProperties(@Header("Authorization") authToken: String): Call<Properties>
//    fun getAllCampaigns() : Call<MutableList<PostedMaps>>



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

//    @POST("postedmaps/postedmap")
//    //  fun createProperty(@Header("Authorization") authToken: String, @Body newProperty: NewProperty): Call<Void>
//    fun createNewMap(@Body newPostedMap: NewPostedMap) : Call<Void>

    @GET("users/user/name/{userName}")

    fun getUser(@Path("userName")username: String): Call<UserResult>

    @DELETE("postedmaps/postedmap/{id}")
    fun deleteCampaign(@Path("id") id: Long) : Call<Void>

    @GET("users/user/name/{userName}")
    fun getUser2(@Path("userName")username: String): Call<User>

    @GET("users/user/name/{userName}")
    fun getUser3(@Path("userName")username: String): Call<User>


//    @GET("campaigns/campaign/title/like/{title}")
//    fun getFoundUser(@Path("title") title: String): Observable<MutableList<PostedMaps>>
//
//
//    @GET("campaigns/campaign/event_name/like/{eventname}")
//    fun getEventName(@Path("eventname") eventname: String): Observable<MutableList<PostedMaps>>
    companion object {

        const val BASE_URL = "https://ay-my-location.herokuapp.com/"

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .baseUrl(BASE_URL)
            .build()


    }

}