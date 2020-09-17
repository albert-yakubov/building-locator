package com.stepashka.buildinglocator2.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResponce {
    /*This name "error" must match the message key returned by the server.
     Example: {"error": "Bad Request ....."}*/
    @SerializedName("error")
    @Expose
    var error: String? = null
}

