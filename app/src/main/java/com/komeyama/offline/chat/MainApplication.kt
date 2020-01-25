package com.komeyama.offline.chat

import android.app.Application
import com.facebook.stetho.Stetho
import com.komeyama.offline.chat.di.AppComponent
import com.komeyama.offline.chat.di.DaggerAppComponent
import timber.log.Timber

class MainApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        // Dagger
        appComponent = DaggerAppComponent.factory().create(this)

        // Timber
        Timber.plant(Timber.DebugTree())

        // Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                    Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                    Stetho.defaultInspectorModulesProvider(this))
                .build())
    }

}