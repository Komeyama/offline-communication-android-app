package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NearbyModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideNearbyClient(application: Application, moshi: Moshi) :NearbyClient {
        return NearbyClient(application, moshi)
    }

}