package com.stepashka.buildinglocator2.dataMVVM.apiMVVM

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.stepashka.buildinglocator2.models.PostedMaps
import io.reactivex.Single

class ApiServiceImpl  : ApiService{
    override fun getAllMaps(): Single<List<PostedMaps>> {
        return Rx2AndroidNetworking.get("https://ay-my-location.herokuapp.com/postedmaps/postedmaps")
            .build()
            .getObjectListSingle(PostedMaps::class.java)
    }

}