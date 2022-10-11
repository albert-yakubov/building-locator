package com.stepashka.buildinglocator2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.stepashka.buildinglocator2.adapter.RecyclerViewAdapter
import com.stepashka.buildinglocator2.models.PostedMaps
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowLongClickListener {

    //static for location access
    var ACCESSLOCATION = 123

    var location: Location?=null
    //check permissions on older versions
    companion object {
        const val TAGUSER = ""
        var maps: MutableList<PostedMaps>? = null
        var users: MutableList<User>? =null
        var ID : Long = 12134556456
        var mapTitle = ""
        var TAGADDRESS = ""
        const val NOTIFICATION_ID = 1

        fun checkPermission(mapsActivity: MapsActivity){
            //check permissions on older versions
            if(Build.VERSION.SDK_INT >=23) {

                if (ActivityCompat.checkSelfPermission(
                        mapsActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mapsActivity.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        mapsActivity.ACCESSLOCATION
                    )
                    return

                }
            }
            mapsActivity.getUserLocation()

        }
    }
    private lateinit var mMap: GoogleMap

    //location request
    internal lateinit var mLocationResult: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        mapSearchButton.setOnClickListener {
            SearchLocation()
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Companion.checkPermission(this)
        loadMap()
        load()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowLongClickListener(this)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        loadMap()
        load()
    }

    override fun onInfoWindowLongClick(p0: Marker) {
        val  iD = (p0.snippet?.toLong())
        RecyclerViewAdapter.mapid = (p0.snippet?.toLong()!!)
        val intent = Intent(this, DetailActivity::class.java)

        intent.putExtra("key", iD )
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation(){
        Toast.makeText(this,"User Location Access on",Toast.LENGTH_LONG).show()
        //listen for current location
        val myLocation = MyLocationListener()
        //then check permissions again

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0.1f,myLocation)
        //since my marker is constantly moving, i need my own custom thread
        val myThread = MyThread()
        myThread.start()

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            //request code this
            ACCESSLOCATION -> {
                //and all permissions granted
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getUserLocation()

                } else {
                    Toast.makeText(this, "We can't access your location", Toast.LENGTH_LONG).show()

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    // Get User location
//this is for location thats changing
    inner class MyLocationListener : LocationListener {

        //init stands for primary constructor, used when there are no other constructors
        init {
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(p0: Location) {
            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    //this is for my thread
    var oldLocation:Location?=null
    inner class MyThread : Thread() {
        init {

            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }



        override fun run(){
            while(true){
                try {
                    if(location?.let { oldLocation!!.distanceTo(it) } ==0f){
                        continue //continue auto jumps to most updated loop
                    }
                    oldLocation = location

                    runOnUiThread {
                        mMap.clear()

                        //show my location
                        val myLocation = LatLng(location!!.latitude, location!!.longitude)

                        mMap.addMarker(MarkerOptions()
                            .position(myLocation)
                            .title("Me")
                            .snippet("This is my Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.checkmark_24)))

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14f))




                    }
                    Thread.sleep(10000000)
                }catch (ex:Exception){
                }
            }
        }
    }
    private fun loadMap(){
        val call: Call<MutableList<PostedMaps>> = ServiceBuilder.create().getAllMaps()

        call.enqueue(object: Callback<MutableList<PostedMaps>> {
            override fun onFailure(call: Call<MutableList<PostedMaps>>, t: Throwable) {

                Log.i("TAGG", "onFailure ${t.message.toString()}")
            }

            override fun onResponse(call: Call<MutableList<PostedMaps>>, response: Response<MutableList<PostedMaps>>) {
                if(response.isSuccessful){


                    val list = response.body()
                    maps = list
                    // for list of pokemons
                    for(i in 0 until maps!!.size ) {
                        var newMap = maps!![i]
                        //if not caught
                        val mapLocation =
                            LatLng(newMap.latitude ?: 0.0, newMap.longitude ?: 0.0)
                        val iD =  newMap.id  ?: 9L
                        val mapTitle = newMap.title ?: ""
                        val mapDetails = newMap.comments ?: ""
                        val mapAddress = newMap.address ?:""
                        mMap.addMarker(
                            MarkerOptions()
                                .position(mapLocation)
                                .zIndex(+10f)
                                .title(mapAddress)
                                .snippet(iD.toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.checkmark_24))
                        )

                        var mapLoc = Location("PostedMaps")
                        mapLoc.latitude = newMap.latitude ?: 0.0
                        mapLoc.longitude = newMap.longitude ?: 0.0

//                        if(location!!.distanceTo(  mapLoc )>2) {
//                            Notification.Notification(this@MapsActivity)
//
//                        }

//if you are within 2 ft of pokemon he is yours
                        /*if(location!!.distanceTo((newCampaign.location))<2){
                                newCampaign.isCatched = true
                                // mark the pokemon caught
                                listCampaigns[i]= newCampaign
                                //increase tho points
                                playerPower += newCampaign.power!!
                                Toast.makeText(applicationContext,
                                    "You have caught up a new pockemon, your new power is $playerPower",Toast.LENGTH_LONG).show()
                            }*/

                    }








                }
                else{
                    loadMap()
                    Log.i("Properties ", "OnResponseFailure ${response.errorBody()}")
                }
            }

        })

    }
    private fun load(){

        maps
        users

    }
    fun SearchLocation() {
        var location: String = editText?.text.toString()
        var addressList: List<Address>? = null
        if (location == "") {
            Toast.makeText(applicationContext, "provide location city", Toast.LENGTH_SHORT)
                .show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 4)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(location))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            Toast.makeText(
                applicationContext,
                address.latitude.toString() + " " + address.longitude,
                Toast.LENGTH_LONG
            ).show()
        }
        loadMap()
        load()

    }


}