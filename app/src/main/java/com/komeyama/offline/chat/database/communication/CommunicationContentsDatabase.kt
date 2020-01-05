package com.komeyama.offline.chat.database.communication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommunicationContentsEntities::class], version = 1)
abstract class CommunicationContentsDatabase : RoomDatabase() {
    abstract val communicationContents: CommunicationContentsDao
}