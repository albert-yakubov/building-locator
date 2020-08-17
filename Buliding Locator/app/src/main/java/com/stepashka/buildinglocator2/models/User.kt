package com.stepashka.buildinglocator2.models

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stepashka.buildinglocator2.enum.errorcodes
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
class UserObservable(private var username: String, private var password: String) : BaseObservable() {
    fun getpasssword(): String {
        return this.password
    }

    fun getusername(): String {
        return this.username
    }

    fun setpassword(password: String) {
        this.password = password
    }

    fun setusername(username: String) {
        this.username = username
    }

    fun isvalidData(): errorcodes {

        if (TextUtils.isEmpty(password))
            return errorcodes.passwordempty
        else if (TextUtils.isEmpty(username))
            return errorcodes.username_empty
        else if (password.length < 5)
            return errorcodes.invalid_password_length_less5
        else
            return errorcodes.valid
    }
}




data class Result(val username: String? = null, val userid: Long? = null, val ulatitude: Double? = null, val ulongitude: Double? = null)
data class UserResult(val username: String? = null, val result: Result, val  userid: Long? = null, val ulatitude: Double? = null, val ulongitude: Double? = null)
