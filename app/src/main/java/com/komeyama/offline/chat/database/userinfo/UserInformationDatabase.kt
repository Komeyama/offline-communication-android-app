package com.komeyama.offline.chat.database.userinfo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserInformationEntities::class], version = 1)
abstract class UserInformationDatabase: RoomDatabase() {
    abstract val userInformationDao: UserInformationDao
}