package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NearbyModule {

    @Singleton
    @Provides
    fun provideNearbyClient(application: Application) :NearbyClient {
        return NearbyClient(application)
    }
}