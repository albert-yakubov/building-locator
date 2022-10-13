package com.stepashka.buildinglocator2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepashka.buildinglocator2.databinding.ActivityLoginBinding
import com.stepashka.buildinglocator2.loginMVVMnetwork.AuthListener
import com.stepashka.buildinglocator2.loginMVVMnetwork.AuthViewModel
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.models.UserResult
import com.stepashka.buildinglocator2.services.ServiceBuilder
import com.stepashka.buildinglocator2.util.CustomeProgressDialog
import com.stepashka.buildinglocator2.util.Util
import com.stepashka.buildinglocator2.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), AuthListener {
    private val TAG = "LoginActivity"

    companion object {
        var successfulLogin: Boolean = false
        var content_type = "application/x-www-form-urlencoded"

        //var content_type = "application/json"
        const val CLIENT_ID = R.string.miinibio
        const val CLIENT_SECRET = R.string.miinisecret


        var authString = "$CLIENT_ID:$CLIENT_SECRET"
        var encodedAuthString: String =
            Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
        var auth = "Basic $encodedAuthString"


        var username = ""
        lateinit var password: String
        var email = ""
        //  var admins : Boolean = false
        //  var userid: Long = 12314546
        //  var ulatitude: Double = 0.0
        //  var ulongitude: Double = 0.0
        //  var username4D: String = ""

        var USER_NAME = ""
    }

//    override var validatedUsername: Boolean = false
//    override var validatedPassword: Boolean = false
//    override var error: Boolean? = false

    var binding: ActivityLoginBinding? = null
    var viewmodel: AuthViewModel? = null

    var customeProgressDialog: CustomeProgressDialog? = null


    // trying to remember the user with shared prefs:
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = getSharedPreferences("login", MODE_PRIVATE)

        if(sp!!.getBoolean("logged",false)){


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewmodel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding?.viewModel = viewmodel
        viewmodel!!.authListener = this
        customeProgressDialog = CustomeProgressDialog(this)

        initObservables()



        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        progress_login.visibility = View.INVISIBLE




        btn_register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }

        btn_reset.setOnClickListener {
            username = text_input_username.editText?.text.toString().trim()
            if (username.isEmpty()) {

                Util.showResetAlert(context = this)

            } else {
                val intent = Intent(this@LoginActivity, ResetPassActivity::class.java)
                startActivity(intent)
            }
        }
        btn_reset.setOnClickListener {
            username = text_input_username.editText?.text.toString().trim()
            if (username.isEmpty()) {

                Util.showResetAlert(context = this)

            } else {
                val intent = Intent(this@LoginActivity, ResetPassActivity::class.java)
                startActivity(intent)
            }
        }


    }

    override fun onStarted() {
        toast("logging in...")
    }

    override fun onSuccess(loginResponse: LiveData<String>) {
        loginResponse.observe(this, Observer {
            toast(it.toString())
            // sending user to tab home activity

            sp?.edit()?.putBoolean("logged",true)?.apply();
            Log.i("login", "logging in here")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()


        })

    }

    override fun onFailure(message: String) {
        toast(message)
    }

    private fun initObservables() {
        viewmodel?.progressDialog?.observe(this, Observer {
            if (it!!) {
                customeProgressDialog?.show()
            } else {
                customeProgressDialog?.dismiss()
            }
        })

        viewmodel?.userLogin?.observe(this, Observer {

            Toast.makeText(this, "welcome, $username", Toast.LENGTH_LONG).show()
        })
    }
    fun getLoggedInUser(){
        val call: Call<User> = ServiceBuilder.create()
            .getUserById(MainActivity.userid)



        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

                Toast.makeText(
                    this@LoginActivity,
                    "You have been logged out",
                    Toast.LENGTH_LONG
                ).show()

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                }

            }

        })
    }
}
