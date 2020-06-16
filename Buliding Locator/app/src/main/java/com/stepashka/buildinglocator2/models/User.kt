package com.stepashka.buildinglocator2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "user")
data class User(
    var userid: Long? = null,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_roomId")
    var userRoomId: Int? = null,
    var profilepicture: String? = null,
    var username: String? = null,
    var password: String? = null,
    var primaryemail: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var mini_bio: String? = null,

    var location: String? = null,
    var ulatitude: Double? = null,
    var ulongitude: Double? = null
    //  @SerializedName("following")
    //  var followingUsers: MutableList<User> = mutableListOf(),
    //  @SerializedName("followers")
    //  var followerUsers: MutableList<User> = mutableListOf()






):Serializable


class UpdateUser(

    var mini_bio: String? = null,
    var location: String? = null,
    var ulatitude: Double? = null,
    var ulongitude: Double? = null



)

class ResetPassword(

    var mini_bio: String? = null,
    var location: String? = null,
    var ulatitude: Double? = null,
    var ulongitude: Double? = null,
    var password: String? = null


)





data class Result(val username: String? = null, val userid: Long? = null, val ulatitude: Double? = null, val ulongitude: Double? = null)
data class UserResult(val username: String? = null, val result: Result, val  userid: Long? = null, val ulatitude: Double? = null, val ulongitude: Double? = null)
