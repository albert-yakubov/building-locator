package com.stepashka.buildinglocator2.dataMVVM.apiMVVM

import com.rx2androidnetworking.Rx2ANRequest
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.stepashka.buildinglocator2.models.PostedMaps
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

class ApiServiceImpl  : ApiService{
    override fun getAllMaps(): Single<List<PostedMaps>> {
        return Rx2AndroidNetworking.get("https://ay-my-location.herokuapp.com/postedmaps/postedmaps")
            .build()
            .getObjectListSingle(PostedMaps::class.java)
    }

    override fun getMapByTitle(): Observable<MutableList<PostedMaps>>? {
        return Rx2AndroidNetworking.get("https://ay-my-location.herokuapp.com/postedmaps/postedmaps/title/like/{title}")
            .build()
            .getObjectListObservable(PostedMaps::class.java)
    }

}