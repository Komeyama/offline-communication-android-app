package com.komeyama.offline.chat.database.communication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommunicationContentsDao {
    @Insert
    fun insert(communicationContentsEntities: CommunicationContentsEntities)

    @Query("SELECT * FROM communication_contents_entities ORDER BY communicationId DESC")
    fun getAllCommunicationList(): List<CommunicationContentsEntities>
}