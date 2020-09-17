package com.stepashka.buildinglocator2.loginMVVMnetwork

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stepashka.buildinglocator2.LoginActivity
import com.stepashka.buildinglocator2.util.Util
import okhttp3.ResponseBody
import org.jetbrains.anko.internals.AnkoInternals.getContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository{
    // this repository will store the callbacks from the loginInterface, & pass the value to viewModel not on TabActivity

    // this function is returning the live data object of type string, which wll be passed to viewModel
    fun userLoginMVVM(auth: String, content_type: String, username: String, password: String): LiveData<String>{
        val loginResponseMVVM = MutableLiveData<String>()

        // inject user login function
        LoginAPI().loginMVVM(auth, content_type, username, password).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loginResponseMVVM.value = t.message
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful){
                    loginResponseMVVM.value = response.body()?.toString()
                }else{
                    loginResponseMVVM.value = response.errorBody()?.toString()
                }
            }
        })
    return loginResponseMVVM
    }


    fun validateUsername(): Boolean {
        //Gets the text from the username text input layout

        return when {
            LoginActivity.username.isEmpty() -> {

                false
            }
            LoginActivity.username.length < 4 -> {
                false
            }
            else -> LoginActivity.username.length <= 12
        }
    }

    fun validatePassword(): Boolean {//Gets the text from the password text input layout

       return when {
            LoginActivity.password.isEmpty() -> {

                false
            }
            LoginActivity.password.length < 4 -> {
                false
            }
            else -> LoginActivity.password.length <= 12
        }
    }
}
