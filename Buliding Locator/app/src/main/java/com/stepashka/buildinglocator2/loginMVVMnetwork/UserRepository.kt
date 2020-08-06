package com.stepashka.buildinglocator2.loginMVVMnetwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository{
    // this repository will store the callbacks from the loginInterface, & pass the value to viewModel not on TabActivity

    // this function is returning the live data object of type string, which wll be passed to viewModel
    fun userLoginMVVM(auth: String, content_type: String, username: String, password: String): LiveData<String>{
        val loginResponseMVVM = MutableLiveData<String>()

        // inject user login function
        MyAPI().loginMVVM(auth, content_type, username, password).enqueue(object : Callback<ResponseBody>{
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

}