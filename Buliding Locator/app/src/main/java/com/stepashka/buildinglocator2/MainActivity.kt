package com.stepashka.buildinglocator2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.stepashka.buildinglocator2.adapter.RecyclerViewAdapter
import com.stepashka.buildinglocator2.models.PostedMaps
import com.stepashka.buildinglocator2.models.UserResult
import com.stepashka.buildinglocator2.services.LoginServiceSql
import com.stepashka.buildinglocator2.services.ServiceBuilder
import com.stepashka.buildinglocator2.util.AppController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MainActivity : AppCompatActivity(){


    private lateinit var disposable: Disposable
    private lateinit var disposable2: Disposable


    @Inject
    lateinit var callService: LoginServiceSql
    companion object{
        var map: MutableList<PostedMaps>? = null
        var title: String = ""
        var userid: Long = 12314546
        var ulatitude: Double = 0.0
        var ulongitude: Double = 0.0
        var username4D: String = ""
        lateinit var username: String
        var MAPID: Long = 1
    }
    private lateinit var handler: Handler
    private lateinit var r: Runnable
    lateinit var password: String
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        handler = Handler()
        r = Runnable {
            // TODO Auto-generated method stub
            Toast.makeText(this@MainActivity, "user is inactive from last 5 minutes", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        startHandler()
        getUser()


        (application as AppController).appComponent.inject(this)
        searchButton2.setOnClickListener {
            val searchedFor = enterText.text.toString()
            if (searchedFor.isNotEmpty() && searchedFor.contains(searchedFor)) {

                disposable = callService.getTitle(searchedFor)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ title   ->
                        if (title.isNotEmpty() ) {
                            vRecycle.adapter = RecyclerViewAdapter(title)
                        } else {
                            Toast.makeText(this, "title not found", Toast.LENGTH_SHORT).show()
                        }
                    }, { t ->
                        Log.i("Retrofit - ", "$t", t)
                    })
            }else {
                Toast.makeText(this, "Please enter something in search", Toast.LENGTH_SHORT).show()
            }
        }

        searchButton2.setOnLongClickListener {
            val searchedFor = enterText.text.toString()
            if (searchedFor.isNotEmpty() && searchedFor.contains(searchedFor)) {

                disposable2 = callService.getAddress(searchedFor)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ eventname   ->
                        if (title.isNotEmpty() ) {
                            vRecycle.adapter = RecyclerViewAdapter(eventname)
                        } else {
                            Toast.makeText(this, "address not found", Toast.LENGTH_SHORT).show()
                        }
                    }, { t ->
                        Log.i("Retrofit - ", "$t", t)
                    })
            }else {
                Toast.makeText(this, "Please enter something in search", Toast.LENGTH_SHORT).show()
            }
            return@setOnLongClickListener true

        }



        view_floatingbutton.setOnClickListener {
            val intent = Intent(this, PostMapActivity::class.java)
            startActivity(intent)
        }

        getAllMaps()


    }

    override fun onStart() {
        super.onStart()
        view_floatingbutton.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        view_floatingbutton.visibility = View.VISIBLE

    }
    fun getUser(){


        val call: Call<UserResult> = ServiceBuilder.create().getUser(LoginActivity.username)
        call.enqueue(object: Callback<UserResult> {
            override fun onFailure(call: Call<UserResult>, t: Throwable) {
                Log.i("Login:", "OnFailure ${t.message.toString()}")
                Toast.makeText(this@MainActivity, "Connection Timed Out! Try Again!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserResult>, response: Response<UserResult>) {
                if(response.isSuccessful) {

                    userid = response.body()?.userid ?: 1231234
                    ulatitude = response.body()?.ulatitude ?: 0.0
                    ulongitude = response.body()?.ulongitude ?: 0.0
                    username4D = response.body()?.username ?: ""
                    username = response.body()?.username ?: ""

                    Log.i("Login", "Success ${response.body()}")


                }
                else{
                    Log.i("Login", "Failure ${response.errorBody().toString()}")
                    Toast.makeText(this@MainActivity, "Invalid Login info!", Toast.LENGTH_LONG).show()

                }
            }

        })
    }
    fun getMapByAddress(){
        val call: Call<PostedMaps> = ServiceBuilder.create().getById(MAPID)

        call.enqueue(object: Callback<PostedMaps> {
            override fun onFailure(call: Call<PostedMaps>, t: Throwable) {
            }


            override fun onResponse(call: Call<PostedMaps>, response: Response<PostedMaps>) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                startActivity(intent)
            }

        })


    }
    fun getAllMaps(){
        val call: Call<MutableList<PostedMaps>> = ServiceBuilder.create().getAllMaps()

        call.enqueue(object: Callback<MutableList<PostedMaps>> {
            override fun onFailure(call: Call<MutableList<PostedMaps>>, t: Throwable) {
            }

            override fun onResponse(call: Call<MutableList<PostedMaps>>, response: Response<MutableList<PostedMaps>>) {
                if(response.isSuccessful){

                    val list = response.body()

                    map = list

                    vRecycle?.apply {
                        vRecycle.itemAnimator = DefaultItemAnimator()
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        (layoutManager as LinearLayoutManager).isSmoothScrollbarEnabled = true
                        (layoutManager as LinearLayoutManager).stackFromEnd = true
                        (layoutManager as LinearLayoutManager).supportsPredictiveItemAnimations()


                        adapter = RecyclerViewAdapter(map)


                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "Nooooo", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }



    override fun onUserInteraction() { // TODO Auto-generated method stub
        super.onUserInteraction()
        stopHandler() //stop first and then start
        startHandler()
    }
    fun stopHandler() {
        handler.removeCallbacks(r)
    }
    fun startHandler() {
        handler.postDelayed(r, 5 * 60 * 1000.toLong()) //for 5 minutes
    }
}
