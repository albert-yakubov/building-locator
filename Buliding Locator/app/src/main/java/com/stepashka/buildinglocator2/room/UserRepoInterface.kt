package com.stepashka.buildinglocator2.room


import androidx.lifecycle.LiveData
import com.stepashka.buildinglocator2.base.BaseRepoInterface
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.models.UserLogin


interface UserRepoInterface : BaseRepoInterface<User> {

    fun getUserData(id: Int): LiveData<User>

    fun loginUser(user: UserLogin)

    fun nukeUserTable()

    fun deleteOldUsers()
}