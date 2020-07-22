package com.stepashka.buildinglocator2.dataMVVM.RepositoryMVVM

import com.stepashka.buildinglocator2.dataMVVM.apiMVVM.ApiHelper
import com.stepashka.buildinglocator2.models.PostedMaps
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper){
    fun getPostedMaps() : Single<List<PostedMaps>>{
        return apiHelper.getPostedMaps()
    }

    fun getMapsByTitle() : Observable<MutableList<PostedMaps>>? {
        return apiHelper.getMapsByTitle()

    }
}