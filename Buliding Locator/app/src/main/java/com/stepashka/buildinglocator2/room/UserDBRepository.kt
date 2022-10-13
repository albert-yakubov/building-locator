package com.stepashka.buildinglocator2.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.stepashka.buildinglocator2.models.User
import com.stepashka.buildinglocator2.models.UserLogin


class UserDBRepository(val context: Context) : UserRepoInterface {
    override fun loginUser(user: UserLogin) {
    }

    private val database = AppDB.getDatabase(context)
    private val apiFactory = apiFactory()


    // Room
    override fun getUserData(id: Int): LiveData<UserForDAO> {
        return database.userDao().getUserData(id)
    }

    override fun create(obj: UserForDAO) {
        database.userDao().insert(obj)
    }

    override fun update(obj: UserForDAO) {
        database.userDao().update(obj)
    }

    override fun delete(obj: UserForDAO) {
        database.userDao().delete(obj)
    }

    override fun nukeUserTable() {
        database.userDao().nukeUserTable()
    }

    override fun deleteOldUsers() {
        database.userDao().deleteOldUsers()
    }
}