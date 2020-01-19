package com.komeyama.offline.chat.database.userinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_information_entities")
data class UserInformationEntities (
    @PrimaryKey
    val databaseId: Int = 0,
    val userId: String = "",
    val userName: String
)