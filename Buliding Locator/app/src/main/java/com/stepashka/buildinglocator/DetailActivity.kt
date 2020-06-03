package com.stepashka.buildinglocator

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.stepashka.buildinglocator.adapter.RecyclerViewAdapter
import com.stepashka.buildinglocator.models.PostedMaps
import com.stepashka.buildinglocator.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    companion object {
        var PASSID: Long = 1
        var IMAGEME = ""
    }

    var primaryemail = ""
    var ulatitude: Double = 0.0
    var ulongitude: Double = 0.0


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        getMapById()
    }
        fun getMapById() {

            val call: Call<PostedMaps> = ServiceBuilder.create().getById(RecyclerViewAdapter.mapid)

            call.enqueue(object : Callback<PostedMaps> {
                override fun onFailure(call: Call<PostedMaps>, t: Throwable) {
                    Log.i("User ", "onFailure ${t.message.toString()}")
                }

                override fun onResponse(call: Call<PostedMaps>, response: Response<PostedMaps>) {
                    if (response.isSuccessful) {

                        val profilePictureSfx = response.body()?.map ?: ""

                        if ((profilePictureSfx.endsWith("jpeg")) ||
                            (profilePictureSfx.endsWith("jpg")) ||
                            (profilePictureSfx.endsWith("png")) ||
                            (profilePictureSfx.contains("auto"))
                        ) {
                            Picasso.get().load(profilePictureSfx).into(MAPME)
                            Log.i("profile pic", profilePictureSfx)

                        }

                    } else {
                        Log.i("Something Went Wrong ", "OnResponseFailure ${response.errorBody()}")
                    }

                }

            })

        }
    }



//    private fun sendEmail(recipient: String, subject: String, message: String) {
//        /*ACTION_SEND action to launch an email client installed on your Android device.*/
//        val mIntent = Intent(Intent.ACTION_SEND)
//        /*To send an email you need to specify mailto: as URI using setData() method
//        and data type will be to text/plain using setType() method*/
//        mIntent.data = Uri.parse("mailto:")
//        mIntent.type = "text/plain"
//        // put recipient email in intent
//        /* recipient is put as array because you may wanna send email to multiple emails
//           so enter comma(,) separated emails, it will be stored in array*/
//        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(primaryemail))
//        //put the Subject in the intent
//        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello Key")
//        //put the message in the intent
//        mIntent.putExtra(Intent.EXTRA_TEXT, "Dear Key")
//
//
//        try {
//            //start email intent
//            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
//        }
//        catch (e: Exception){
//            //if any thing goes wrong for example no email client application or any exception
//            //get and show exception message
//            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
//        }
//
//    }

