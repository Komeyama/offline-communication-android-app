package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListFragment
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListFragment
import com.komeyama.offline.chat.ui.MainActivity
import com.komeyama.offline.chat.ui.setting.SettingFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules=[AppModule::class])
interface AppComponent {

    fun injectionToMainActivity(activity: MainActivity)

    fun injectionToCommunicableUserListFragment(fragment: CommunicableUserListFragment)

    fun injectionToCommunicationHistoryFragment(fragment: CommunicationHistoryListFragment)

    fun injectionToSettingFragment(fragment: SettingFragment)

    @Component.Builder
    interface Builder{
        fun build(): AppComponent

        @BindsInstance
        fun application(application:Application): Builder
    }

}