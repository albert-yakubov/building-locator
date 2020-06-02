package com.stepashka.buildinglocator.room


import androidx.lifecycle.LiveData
import com.stepashka.buildinglocator.base.BaseRepoInterface
import com.stepashka.buildinglocator.models.User
import com.stepashka.buildinglocator.models.UserLogin


interface UserRepoInterface : BaseRepoInterface<User> {

    fun getUserData(id: Int): LiveData<User>

    fun loginUser(user: UserLogin)

    fun nukeUserTable()

    fun deleteOldUsers()
}