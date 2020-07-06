package com.stepashka.buildinglocator2.dataMVVM.apiMVVM

class ApiHelper(private val apiService: ApiService){
    fun getPostedMaps() = apiService.getAllMaps()
}