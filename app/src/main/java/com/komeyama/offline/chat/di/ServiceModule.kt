package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.service.NearbyService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {

    @Singleton
    @Provides
    fun provideNearbyService(application: Application) :NearbyService {
        return NearbyService(application)
    }
}