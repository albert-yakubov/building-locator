package com.stepashka.buildinglocator2.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewUser (

    @SerializedName("firstname")
    var firstName: String?,
    @SerializedName("lastname")
    var lastName: String?,
    @SerializedName("primaryemail")
    var email: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("position")
    var position: Boolean
) : Serializable
class Neweruser(
    var profilepicture: String? = null,
    var username: String? = null,
    var password: String? = null,
    var primaryemail: String? = null,

    var firstname: String? = null,
    var lastname: String? = null,

    var mini_bio: String? = null,

    var location: String? = null,
    var ulatitude: Double? = null,
    var ulongitude: Double? = null
):Serializable