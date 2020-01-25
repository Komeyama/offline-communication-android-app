package com.komeyama.offline.chat.database.userinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserInformationDao {
    @Insert
    fun insert(userInformationEntities: UserInformationEntities)

    @Update
    fun update(userInformationEntities: UserInformationEntities)

    @Query("SELECT * FROM user_information_entities ORDER BY databaseId DESC")
    fun getUserInformation(): UserInformationEntities

    @Query("SELECT COUNT(*) FROM user_information_entities WHERE databaseId = 0")
    fun existsUserInformation(): Boolean

}