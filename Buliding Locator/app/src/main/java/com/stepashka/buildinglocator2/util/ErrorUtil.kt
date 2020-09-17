package com.stepashka.buildinglocator2.util

import com.stepashka.buildinglocator2.models.ErrorResponce
import com.stepashka.buildinglocator2.services.ApiClient
import com.stepashka.buildinglocator2.services.ServiceBuilder
import retrofit2.Response
import java.io.IOException

object ErrorUtils {

    fun parseError(response: Response<*>): ErrorResponce? {
        val conversorDeErro = ApiClient.apiClient()
            .responseBodyConverter<ErrorResponce>(ErrorResponce::class.java, arrayOfNulls(0))
        var errorResponce: ErrorResponce? = null
        try {
            if (response.errorBody() != null) {
                errorResponce = conversorDeErro.convert(response.errorBody()!!)
            }
        } catch (e: IOException) {
            return ErrorResponce()
        } finally {
            return errorResponce
        }
    }

}