package com.komeyama.offline.chat

import android.app.Application
import com.komeyama.offline.chat.di.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules=[
        AppModule::class,
        ViewModelModule::class,
        UserInfoDatabaseModule::class,
        CommunicationDatabaseModule::class,
        CommunicatedUserDatabaseModule::class,
        NearbyModule::class
    ]
)
interface TestAppComponent : AppComponent {
    fun injectionMenuScreenTransitionTest(activity: MenuScreenTransitionTest)

    fun injectionCommunicationScreenTest(activity: CommunicationScreenTest)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): TestAppComponent
    }
}