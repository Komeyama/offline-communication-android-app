package com.komeyama.offline.chat.di

import android.app.Application
import androidx.room.Room
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.communication.CommunicationContentsDatabase
import com.komeyama.offline.chat.repository.CommunicationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommunicationDatabeseModule {
    @Singleton
    @Provides
    fun provideCommunicationContentsDatabase(application: Application): CommunicationContentsDatabase {
        return Room.databaseBuilder(application, CommunicationContentsDatabase::class.java, "communication_contents").build()
    }

    @Singleton
    @Provides
    fun provideCommunicationContentsDao(db: CommunicationContentsDatabase): CommunicationContentsDao {
        return db.communicationContents
    }

    @Singleton
    @Provides
    fun provideCommunicationRepository(dao: CommunicationContentsDao): CommunicationRepository {
        return CommunicationRepository(dao)
    }

}