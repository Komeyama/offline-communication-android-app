package com.komeyama.offline.chat

import android.app.Application
import com.komeyama.offline.chat.di.AppComponent
import com.komeyama.offline.chat.di.DaggerAppComponent
import timber.log.Timber

class MainApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appComponent = DaggerAppComponent.factory().create(this)
    }

}