package com.komeyama.offline.chat.di

import android.app.Application
import com.komeyama.offline.chat.ui.MainActivity
import com.komeyama.offline.chat.ui.dialog.ConfirmAcceptanceDialog
import com.komeyama.offline.chat.ui.dialog.ConfirmFinishCommunicationDialog
import com.komeyama.offline.chat.ui.dialog.ConfirmRequestDialog
import com.komeyama.offline.chat.ui.dialog.InitialSettingDialog
import com.komeyama.offline.chat.ui.fragment.*
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
interface AppComponent {

    fun injectionToMainActivity(activity: MainActivity)

    fun injectionToCommunicableUserListFragment(fragment: CommunicableUserListFragment)

    fun injectionToCommunicationHistoryFragment(fragment: CommunicationHistoryListFragment)

    fun injectionToSettingFragment(fragment: SettingFragment)

    fun injectionToCommunicationFragment(fragment: CommunicationFragment)

    fun injectionToCommunicationHistoryFragment(fragment: CommunicationHistoryFragment)

    fun injectionToConfirmAcceptanceDialog(fragment: ConfirmAcceptanceDialog)

    fun injectionToConfirmRequestDialog(fragment: ConfirmRequestDialog)

    fun injectionToConfirmFinishCommunicationDialog(fragment: ConfirmFinishCommunicationDialog)

    fun injectionToInitialSettingDialog(fragment: InitialSettingDialog)

    fun injectionToLicenseFragment(fragment: LicenseFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application:Application): AppComponent
    }

}