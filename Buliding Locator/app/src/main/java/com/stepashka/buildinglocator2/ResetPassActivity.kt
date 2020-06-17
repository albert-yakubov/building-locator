package com.stepashka.buildinglocator2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.stepashka.buildinglocator2.models.ResetPassword
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_reset_pass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassActivity : AppCompatActivity() {

    var username = ""
    var password: String = ""
    var userid : Long = 235
    var minibio: String = ""


    var location :String= ""
    var ulatitude: Double = 0.0
    var ulongitude: Double = 0.0



    var minibio2 = "I am not so nice"

    var location2 = "Denver"
    var ulatitude2: Double = 0.0
    var ulongitude2: Double = 0.0
    var password2 = "1234564"

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please Wait")
        builder.setMessage("Please wait while we verify your info...")
        builder.setNegativeButton("OK"){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.show()



        progress_user.visibility = View.VISIBLE
        getUser()


        btn_reset2.setOnClickListener {
            minibio2 = minibio

            location2 = location
            ulatitude2 = MainActivity.ulatitude
            ulongitude2 = MainActivity.ulongitude

            updateUserById()
        }


    }
    fun updateUserById(){
        //text_input_username.editText?.text.toString().trim()
        password2 = reset_text_input_password2.editText?.text.toString().trim()
        val call: Call<Void> = ServiceBuilder.create().updateUserPById(userid, ResetPassword(minibio2,
           location2, ulatitude2, ulongitude2, password2)
        )


        call.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ResetPassActivity, "Failed to connect", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){



                    //   UpdateUser(userid, minibio, species,facebook,instagram,twitter,location,latitude,longitude)
                    //code goes here
                    Toast.makeText(this@ResetPassActivity, "Addded", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ResetPassActivity, LoginActivity::class.java)
                    startActivity(intent)

                }
                else{
                    Toast.makeText(this@ResetPassActivity, "Failed to connect", Toast.LENGTH_SHORT).show()
                }

            }

        })

    }
    fun getUser(){


        val call: Call<User> = ServiceBuilder.create().getUser3(LoginActivity.username)
        call.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@ResetPassActivity, "Connection Timed Out! Try Again!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    progress_user.visibility = View.GONE




                    userid = response.body()?.userid ?: 1231234
                    minibio = response.body()?.mini_bio ?: ""

                    location = response.body()?.location ?: ""
                    ulatitude = MainActivity.ulatitude
                    ulongitude = MainActivity.ulongitude

                    username = response.body()?.username ?: ""
                    reset_text_input_username2.text = username
                    Toast.makeText(this@ResetPassActivity, "Connected!", Toast.LENGTH_SHORT).show()


                }
                else{
                    Toast.makeText(this@ResetPassActivity, "Invalid Login info!", Toast.LENGTH_LONG).show()

                }
            }

        })
    }
}
