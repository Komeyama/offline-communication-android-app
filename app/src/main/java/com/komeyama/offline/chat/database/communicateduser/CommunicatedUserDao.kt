package com.komeyama.offline.chat.database.communicateduser

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommunicatedUserDao {
    @Insert
    fun insert(communicatedUserEntities: CommunicatedUserEntities)

    @Query("UPDATE communicated_user_entities SET latestDate=:date where communicatedUserId=:communicatedUserId")
    fun updateDate(communicatedUserId:String, date:String)

    @Query("SELECT * FROM communicated_user_entities ORDER BY databaseId DESC")
    fun getCommunicatedUserListLiveData(): LiveData<List<CommunicatedUserEntities>>

    @Query("SELECT * FROM communicated_user_entities ORDER BY databaseId DESC")
    fun getCommunicatedUserList(): List<CommunicatedUserEntities>
}