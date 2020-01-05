package com.komeyama.offline.chat.di

import android.content.Context
import com.komeyama.offline.chat.MainApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: MainApplication): Context {
        return application.applicationContext
    }

}