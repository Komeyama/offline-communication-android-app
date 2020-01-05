package com.komeyama.offline.chat.di

import android.app.Application
import androidx.room.Room
import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.database.userinfo.UserInformationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserInfoDatabaseModule {

    @Singleton
    @Provides
    fun provideUserInformationDatabase(application: Application): UserInformationDatabase {
        return Room.databaseBuilder(application, UserInformationDatabase::class.java, "user_information").build()
    }

    @Singleton
    @Provides
    fun provideUserInformationDao(db: UserInformationDatabase): UserInformationDao {
        return db.userInformationDao
    }

}