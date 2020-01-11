package com.komeyama.offline.chat.database.communication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommunicationContentsDao {
    @Insert
    fun insert(communicationContentsEntities: CommunicationContentsEntities)

    @Query("SELECT * FROM communication_contents_entities ORDER BY contentId DESC")
    fun getAllCommunicationList(): LiveData<List<CommunicationContentsEntities>>

    @Query("SELECT * FROM communication_contents_entities ORDER BY contentId DESC LIMIT 1")
    fun getLatestCommunication(): LiveData<CommunicationContentsEntities>
}