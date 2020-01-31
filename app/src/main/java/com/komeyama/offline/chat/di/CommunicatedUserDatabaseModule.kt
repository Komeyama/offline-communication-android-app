package com.komeyama.offline.chat.di

import android.app.Application
import androidx.room.Room
import com.komeyama.offline.chat.database.communicateduser.CommunicatedUserDao
import com.komeyama.offline.chat.database.communicateduser.CommunicatedUserDatabase
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.repository.CommunicatedUserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommunicatedUserDatabaseModule {
    @Singleton
    @Provides
    fun provideCommunicatedUserDatabase(application: Application): CommunicatedUserDatabase {
        return Room.databaseBuilder(application, CommunicatedUserDatabase::class.java, "communicated_user_contents").build()
    }

    @Singleton
    @Provides
    fun provideCommunicatedUserDao(db: CommunicatedUserDatabase): CommunicatedUserDao {
        return db.communicatedUserDao
    }

    @Singleton
    @Provides
    fun provideCommunicatedUserRepository(dao: CommunicatedUserDao, nearbyClient: NearbyClient): CommunicatedUserRepository {
        return CommunicatedUserRepository(dao, nearbyClient)
    }
}