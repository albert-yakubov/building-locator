package com.stepashka.buildinglocator2.room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.stepashka.buildinglocator2.base.BaseDao
import com.stepashka.buildinglocator2.models.User

@Dao
abstract class UserDAO : BaseDao<User> {

    @Query("SELECT * FROM user")
    abstract fun getAllStoredUsers() : List<User>
    //We only really need one user table since we're only syncing the room database with the API. So...Nuke It!
    @Query("DELETE FROM user")
    abstract fun nukeUserTable()

    @Query("SELECT * FROM user WHERE user_roomId = :userId")
    abstract fun getUserData(userId: Int): LiveData<User>

    @Query("DELETE FROM user where user_roomId NOT IN (SELECT user_roomId from user ORDER BY user_roomId DESC LIMIT 3)")
    abstract fun deleteOldUsers()

}