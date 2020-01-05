package com.komeyama.offline.chat.database.userinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserInformationDao {
    @Insert
    fun insert(userInformationEntities: UserInformationEntities)

    @Query("SELECT * FROM user_information_entities ORDER BY userId DESC")
    fun getUserInformation(): List<UserInformationEntities>
}