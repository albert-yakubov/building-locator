package com.stepashka.buildinglocator2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stepashka.buildinglocator2.dataMVVM.RepositoryMVVM.MainRepository
import com.stepashka.buildinglocator2.models.PostedMaps
import com.stepashka.buildinglocator2.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val maps = MutableLiveData<Resource<List<PostedMaps>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchMaps()
    }

    private fun fetchMaps() {
        maps.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getPostedMaps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mapList ->
                    maps.postValue(Resource.success(mapList))
                }, { throwable ->
                    maps.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }
    private fun fetchMapsByTitle() {
        maps.postValue(Resource.loading(null))
        mainRepository.getMapsByTitle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ userList ->
                maps.postValue(Resource.success(userList))
            }, { throwable ->
                maps.postValue(Resource.error("Something Went Wrong", null))
            })?.let {
                compositeDisposable.add(
                    it
            )
            }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getMaps(): LiveData<Resource<List<PostedMaps>>> {
        return maps
    }

    fun getMapsByTitle() : LiveData<Resource<List<PostedMaps>>>{
        return maps
    }
}