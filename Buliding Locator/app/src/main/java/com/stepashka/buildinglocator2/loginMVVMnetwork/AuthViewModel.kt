package com.stepashka.buildinglocator2.loginMVVMnetwork

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import com.stepashka.buildinglocator2.LoginActivity
import com.stepashka.buildinglocator2.MainActivity
import com.stepashka.buildinglocator2.R
import com.stepashka.buildinglocator2.models.NavUserResult
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.models.UserObservable
import com.stepashka.buildinglocator2.models.UserResult
import com.stepashka.buildinglocator2.services.ServiceBuilder
import com.stepashka.buildinglocator2.util.ErrorUtils
import com.stepashka.buildinglocator2.util.SingleLiveEvent
import com.stepashka.buildinglocator2.util.Util
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class AuthViewModel(application: Application) : AndroidViewModel(application){

    private var context: Context? = null
    var authListener : AuthListener? = null
    fun LoginViewModel(context: Context?) {
        this.context = context
    }
    companion object{

        var successfulLogin: Boolean = false
        var content_type = "application/x-www-form-urlencoded"
        //var content_type = "application/json"
        private const val CLIENT_ID = "lambdaclient"

        private const val CLIENT_SECRET = "lambdasecret"


        var navUsername = ""
        var navEmail = ""
        var navProfilePicture = ""

        var authString = "$CLIENT_ID:$CLIENT_SECRET"
        var encodedAuthString: String = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
        var auth = "Basic $encodedAuthString"
        // login viewModel
        private lateinit var loginViewModel: AuthViewModel

//        var username = ""
//        lateinit var password: String
        //  var admins : Boolean = false
        //  var userid: Long = 12314546
        //  var ulatitude: Double = 0.0
        //  var ulongitude: Double = 0.0
        //  var username4D: String = ""

    }

    var btnSelected: ObservableBoolean? = null
    var username: String? = null
    var password: String? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null

    var userLogin: MutableLiveData<UserObservable>? = null

    init {
        btnSelected = ObservableBoolean(false)
        progressDialog = SingleLiveEvent<Boolean>()
        username = ""
        password = ""
        userLogin = MutableLiveData<UserObservable>()
    }

    fun onEmailChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(s.toString()))


    }

    fun onPasswordChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(s.toString()))


    }

    fun login(){

        getLoggedInUser()
        progressDialog?.value = true
        authListener?.onStarted()
        val call: Call<ResponseBody> = ServiceBuilder.create()
            .login( auth, content_type, username.toString(), password.toString() )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog?.value = false

                login()
                getLoggedInUser()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {


                successfulLogin = response.isSuccessful
                progressDialog?.value = false

                if (successfulLogin) {

                    val loginResponse = UserRepository().userLoginMVVM(
                        auth,
                        content_type,
                        username.toString(),
                        password.toString()
                    )


                    successfulLogin = false
                    authListener?.onSuccess(loginResponse)


                } else {
                    Toast.makeText(
                        this@AuthViewModel.getApplication(),
                        "Please check username and password!",
                        Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun getLoggedInUser(){
        val call: Call<UserResult> = ServiceBuilder.create()
            .getUser("${username}")



        call.enqueue(object : Callback<UserResult> {
            override fun onFailure(call: Call<UserResult>, t: Throwable) {
                Toast.makeText(
                    this@AuthViewModel.getApplication(),
                    "You have been logged out",
                    Toast.LENGTH_LONG
                ).show()

            }

            override fun onResponse(call: Call<UserResult>, response: Response<UserResult>) {
                if (response.isSuccessful) {
                    navUsername = response.body()?.username ?: "you were not logged in"
                    navEmail = response.body()?.primaryemail ?: "you were not logged in"

                    navProfilePicture = if ((response.body()?.profilepicture.toString().endsWith("jpeg")) ||
                        (response.body()?.profilepicture.toString().endsWith("jpg")) ||
                        (response.body()?.profilepicture.toString().endsWith("png")) ||
                        (response.body()?.profilepicture.toString().endsWith("auto"))
                    ) {
                        response.body()?.profilepicture.toString()

                    } else {
                        "https://upload.wikimedia.org/wikipedia/en/d/dc/MichaelScott.png"
                    }
                }

            }

        })
    }

}