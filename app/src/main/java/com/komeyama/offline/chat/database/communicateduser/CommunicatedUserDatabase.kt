package com.komeyama.offline.chat.database.communicateduser

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommunicatedUserEntities::class], version = 1)
abstract class CommunicatedUserDatabase: RoomDatabase() {
    abstract val communicatedUserDao: CommunicatedUserDao
}