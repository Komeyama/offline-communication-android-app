package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListFragment
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListFragment
import com.komeyama.offline.chat.ui.MainActivity
import com.komeyama.offline.chat.ui.setting.SettingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules=[
        AppModule::class,
        ViewModelModule::class,
        CommunicationDatabaseModule::class,
        UserInfoDatabaseModule::class
    ]
)
interface AppComponent {

    fun injectionToMainActivity(activity: MainActivity)

    fun injectionToCommunicableUserListFragment(fragment: CommunicableUserListFragment)

    fun injectionToCommunicationHistoryFragment(fragment: CommunicationHistoryListFragment)

    fun injectionToSettingFragment(fragment: SettingFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application:Application): AppComponent
    }

}