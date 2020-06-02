package com.stepashka.buildinglocator.util

import android.app.Application
import com.cloudinary.android.MediaManager
import com.stepashka.buildinglocator.dagger.DaggerNetworkComponent

class AppController : Application() {

    val appComponent by lazy {
        DaggerNetworkComponent
            .builder()
            .build()
    }
    override fun onCreate() {
        super.onCreate()
        // Initialize Cloudinary
        MediaManager.init(this)
    }
}