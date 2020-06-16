package com.stepashka.buildinglocator2.models

import android.net.Uri
import java.io.Serializable


class ImageData(uriPhoto: Uri?): Serializable {
    var description: String? = null
    var uriToString: String? = uriPhoto.toString()

    fun getUriPath(): Uri {

        return Uri.parse(uriToString)

    }
}