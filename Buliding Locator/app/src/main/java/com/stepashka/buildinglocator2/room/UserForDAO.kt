package com.stepashka.buildinglocator2.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class UserForDAO(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "user_roomId")
    var userRoomId: Int? = null,
    var username: String? = null

): Serializable
