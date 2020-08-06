package com.stepashka.buildinglocator2.loginMVVMnetwork

import android.view.View
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var email : String ? = null
    var password : String ? = null
    var auth: String ? = null
    var content_type: String ? = null
    var authListener : AuthListener? = null

    fun onLoginButtonClick(view: View){
        // login process started
        authListener?.onStarted()
        if (auth.isNullOrEmpty() || content_type.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure("Incorrect email or password. or worse")
            return
        }else{
            // fetching live data from UserRepository
            val loginResponse = UserRepository().userLoginMVVM(auth !!, content_type !!, email !!, password !!)

            // Success listener
            authListener?.onSuccess(loginResponse)

        }

    }

}