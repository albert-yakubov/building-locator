package com.stepashka.buildinglocator2.loginMVVMnetwork

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.stepashka.buildinglocator2.MainActivity

import com.stepashka.buildinglocator2.models.UserObservable
import com.stepashka.buildinglocator2.services.ServiceBuilder
import com.stepashka.buildinglocator2.util.Util
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application), Callback<UserObservable> {

    companion object{
        var successfulLogin: Boolean = false
        var content_type = "application/x-www-form-urlencoded"
        //var content_type = "application/json"
        const val CLIENT_ID = "lambdaclient"
        const val CLIENT_SECRET = "lambdasecret"


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
    var userLogin: MutableLiveData<UserObservable>? = null

    init {
        btnSelected = ObservableBoolean(false)
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


        val call: Call<ResponseBody> = ServiceBuilder.create()
            .login( auth, content_type, username.toString(), password.toString() )

        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {


            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                successfulLogin = response.isSuccessful

                if(successfulLogin){
                    successfulLogin = false

                }
            }

        })
    }

    override fun onFailure(call: Call<UserObservable>, t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onResponse(call: Call<UserObservable>, response: Response<UserObservable>) {
        TODO("Not yet implemented")
    }

}