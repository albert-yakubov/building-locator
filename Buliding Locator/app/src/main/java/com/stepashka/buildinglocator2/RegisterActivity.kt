package com.stepashka.buildinglocator2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.stepashka.buildinglocator2.models.Neweruser
import com.stepashka.buildinglocator2.models.RegisterResponse
import com.stepashka.buildinglocator2.services.ServiceBuilder

import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    companion object{
        var token = ""
        var TAG = ""
        var TAGS =""
        var mCurrentPhotoPath = ""
        val IMG_CODE = 6
    }
    //location:
    private var mLatitudeTextView = ""
    private var mLongitudeTextView = ""
    //tools that I need API client for connection listening
    private var mGoogleApiClient: GoogleApiClient? = null
    //extracting location
    private var mLocation: Location? = null
    //location manager
    private var mLocationManager: LocationManager? = null
    //location listeners
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    //update intervals
    private val UPDATE_INTERVAL = (4 * 1000).toLong()  /* 40 secs */
    private val FASTEST_INTERVAL: Long = 10000 /* 10 sec */
    private var locationManager: LocationManager? = null
    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    //validators (stuff like how long the password can be and so on)
    private var validatedFirstName: Boolean = false
    private var validatedLastName: Boolean = false
    private var validatedUsername: Boolean = false
    private var validatedEmail: Boolean = false
    private var validatedPassword: Boolean = false

    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var username: String
    lateinit var primaryemail: String
    lateinit var password: String
    var position: Boolean = false

    //for the rest of the string calls
    var miniBio = "Great Organizer!"
    var location = ""






    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        TAG = localClassName
        image_input_upload_image2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, IMG_CODE)

            }
        }
        mLatitudeTextView = text_input_lat.editText?.text.toString()
        mLongitudeTextView = text_input_lon.editText?.text.toString()
        //set callbacks on connection to API
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d("no", "no connection to API")
        checkLocation() //check whether location service is enable or not in your  phone


        btn_register_create2.setOnClickListener {
            location = text_input_location2.editText?.text.toString()
            miniBio = text_input_mini_bio2.editText?.text.toString()
            validateFirstName()
            validateLastName()
            validateUsername()
            validateEmail()
            validatePassword()
            confirmRegister()
            validateAdmin()
        }

        btn_register_cancel2.setOnClickListener {
            finish()
        }
    }
    private fun validateAdmin(): Boolean{
        position = false
        return true
    }
    //Checks to see if the entered first name is okay or not.
    private fun validateFirstName(): Boolean {
        //Gets the text from the firstName text input layout
        firstname = text_input_first_name_register2.editText?.text.toString().trim()

        if (firstname.isEmpty()) {
            text_input_first_name_register2.error = "Field can't be empty"
            validatedFirstName = false
            return false
        } else if (firstname.length < 2) {
            text_input_first_name_register2.error = "First name must be at least two characters"
            return false
        } else {
            //Removes the error message if it already exists
            text_input_first_name_register2.error = null
            text_input_first_name_register2.isErrorEnabled = false
            validatedFirstName = true
            return true
        }
    }
    //Checks to see if the entered last name is okay or not.
    private fun validateLastName(): Boolean {
        //Gets the text from the lastName text input layout
        lastname = text_input_last_name_register2.editText?.text.toString().trim()

        if (lastname.isEmpty()) {
            text_input_last_name_register2.error = "Field can't be empty"
            validatedLastName = false
            return false
        } else if (lastname.length < 2) {
            text_input_last_name_register2.error = "Last name must be at least two characters"
            return false
        } else {
            //Removes the error message if it already exists
            text_input_last_name_register2.error = null
            text_input_last_name_register2.isErrorEnabled = false
            validatedLastName = true
            return true
        }
    }
    //Checks to see if the entered email is okay or not.
    private fun validateEmail(): Boolean {
        //Gets the text from the email text input layout
        primaryemail = text_input_email_register2.editText?.text.toString().trim()

        if (primaryemail.isEmpty()) {
            text_input_email_register2.error = "Field can't be empty"
            validatedEmail = false
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(primaryemail).matches()) {
            text_input_email_register2.error = "Invalid Email Format"
            return false
        } else {
            //Removes the error message if it already exists
            text_input_email_register2.error = null
            text_input_email_register2.isErrorEnabled = false
            validatedEmail = true
            return true
        }
    }
    //Checks to see if the entered username is okay or not.
    private fun validateUsername(): Boolean {
        //Gets the text from the username text input layout
        username = text_input_username_register2.editText?.text.toString().trim()

        if (username.isEmpty()) {
            text_input_username_register2.error = "Field can't be empty"
            validatedUsername = false
            return false
        } else if (username.length < 4) {
            text_input_username_register2.error = "Username must be at least four characters"
            return false
        }

        //As of the current time of this else if statement, the current max characters is six.
        //Backend person said that he would change it to 12 in the future.
        else if (username.length > 12) {
            text_input_username_register2.error = "Username can't be more than 12 characters"
            return false
        } else {
            //Removes the error message if it already exists
            text_input_username_register2.error = null
            text_input_username_register2.isErrorEnabled = false
            validatedUsername = true
            return true
        }
    }
    //Checks to see if the entered password is okay or not.
    private fun validatePassword(): Boolean {
        //Gets the text from the password text input layout
        password = text_input_password_register2.editText?.text.toString().trim()

        if (password.isEmpty()) {
            text_input_password_register2.error = "Field can't be empty"
            validatedPassword = false
            return false
        } else if (password.length < 4) {
            text_input_password_register2.error = "Password must be at least four characters"
            return false
        } else if (password.length > 12) {
            text_input_password_register2.error = "Password can't be more than 12 characters"
            return false
        } else {
            //Removes the error message if it already exists
            text_input_password_register2.error = null
            text_input_password_register2.isErrorEnabled = false
            validatedPassword = true
            return true
        }
    }
    //Checks to see if all the fields are correct or not. If so, return back to the login page.
    private fun confirmRegister() {
        //If any of the entered information isn't entered properly, prevent the user from successfully registering.
        if (!validatedFirstName || !validatedLastName || !validatedUsername || !validatedEmail || !validatedPassword){
            return
        }else {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this,
                "New User successfully created\nWelcome $firstname",
                Toast.LENGTH_SHORT
            ).show()
            createUserr()
            finish()
        }
    }







    //for location
    //this is the same as overriding onRequest permission result
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return

        }

        startLocationUpdates()
        mLocation = mGoogleApiClient?.let { LocationServices.FusedLocationApi.getLastLocation(it) }
        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onConnectionSuspended(i: Int) {
        Log.i(TAGS, "Connection Suspended")
        mGoogleApiClient!!.connect()
    }
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAGS, "Connection failed. Error: " + connectionResult.errorCode)
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    //location listener

    override fun onLocationChanged(location: Location) {
        val msg = "Updated Location: " +
                location.latitude.toString() + "," +
                location.longitude.toString()
        mLatitudeTextView = location.latitude.toString()
        mLongitudeTextView = location.longitude.toString()
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location.latitude, location.longitude)
    }
    //location request is being used here for location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mGoogleApiClient?.let {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                it,
                mLocationRequest!!, this
            )
        }
        Log.d("reque", "--->>>>")
    }

    //function to build custom alert box
    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                //send user to the settings screen to change the permission
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }
    private fun checkLocation(): Boolean {
        if (!isLocationEnabled)
            showAlert()
        return isLocationEnabled
    }

    //for prifile pic upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMG_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            var requestId: String = MediaManager.get()
                .upload(uri)
                .option("resource_type", "image")
                .option("connect_timeout", 10000)
                .option("read_timeout", 10000)
                .unsigned("xyqfim3e")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d(TAG, "onStart")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {

                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val publicId:String = resultData["url"] as String
                        mCurrentPhotoPath = publicId.replace("http://", "https://")
                        Toast.makeText(this@RegisterActivity, "Upload successful", Toast.LENGTH_LONG).show()
                    }
                    override fun onError(requestId: String, error: ErrorInfo) {
                        Log.d(TAG,error.description)
                        Toast.makeText(this@RegisterActivity,"Upload was not successful",Toast.LENGTH_LONG).show()
                    }
                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        Log.d(TAG, "onReschedule")
                    }
                })
                .dispatch()
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                // Log.d(TAG, String.valueOf(bitmap));
                val imageView: ImageView = findViewById<ImageView>(R.id.image_input_upload_image2)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun createUserr(){
        val call: Call<RegisterResponse> = ServiceBuilder.create().createUser(
            Neweruser( mCurrentPhotoPath,
            username,
            password,
            primaryemail,
            firstname,
            lastname,
            miniBio,
            location,
            40.0000,
            -120.0001111)
        )

        call.enqueue(object: Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Noooo", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                token = response.body()?.token ?: ""
                Toast.makeText(this@RegisterActivity, "No token", Toast.LENGTH_SHORT).show()
            }
        })
    }




}
