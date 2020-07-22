package com.stepashka.buildinglocator2.models

import java.io.Serializable

data class PostedMaps(
    val id: Long? = null,
    val title: String? = null,
    val address: String? = null,
    val map: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
    val comments: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val created_at: Long? = null,



    val user: MapUser


) : Serializable

data class NewMap(
    var title: String,
    var address: String,
    var map: String,
    var city: String,
    var state: String,
    var zip: String,
    var comments: String,
    var latitude: Double,

    var longitude: Double,

    var created_at: String,

    var user: NewMapUser


) : Serializable

data class MapUser(var userid: Long, var username: String, var  profilepicture: String):Serializable

data class NewMapUser(var userid: Long):Serializable


data class MapResult(val latitude: Double? = null, val longitude: Double? = null)
data class MapResults(val result: MapResult, val latitude: Double? = null, val longitude: Double? = null)