package com.stepashka.buildinglocator2.dataMVVM.apiMVVM

import com.rx2androidnetworking.Rx2ANRequest
import com.stepashka.buildinglocator2.models.PostedMaps
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

interface ApiService{
    fun getAllMaps() : Single<List<PostedMaps>>

    fun getMapByTitle() : Observable<MutableList<PostedMaps>>?
}