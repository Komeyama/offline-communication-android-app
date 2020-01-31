package com.komeyama.offline.chat.database.communicateduser

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CommunicatedUserDao {
    @Insert
    fun insert(communicatedUserEntities: CommunicatedUserEntities)

    @Update
    fun update(communicatedUserEntities: CommunicatedUserEntities)

    @Query("SELECT * FROM communicated_user_entities ORDER BY databaseId DESC")
    fun getCommunicatedUserListLiveData(): LiveData<List<CommunicatedUserEntities>>

    @Query("SELECT * FROM communicated_user_entities ORDER BY databaseId DESC")
    fun getCommunicatedUserList(): List<CommunicatedUserEntities>
}