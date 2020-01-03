package com.komeyama.offline.chat.di

import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListViewModel
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListViewModel
import com.komeyama.offline.chat.ui.MainViewModel
import com.komeyama.offline.chat.ui.setting.SettingViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class AppModule {

    @Provides
    fun provideMainViewModelFactory(mainViewModel: MainViewModel):MainViewModelFactory {
        return MainViewModelFactory(Provider { mainViewModel })
    }

    @Provides
    fun CommunicableUserListViewModelFactory(communicableUserListViewModel: CommunicableUserListViewModel) :CommunicableUserListViewModelFactory{
        return CommunicableUserListViewModelFactory(Provider { communicableUserListViewModel })
    }

    @Provides
    fun CommunicationHistoryListViewModelFactory(communicationHistoryListViewModel: CommunicationHistoryListViewModel) :CommunicationHistoryListViewModelFactory{
        return CommunicationHistoryListViewModelFactory(Provider { communicationHistoryListViewModel })
    }

    @Provides
    fun SettingViewModelFactory(settingViewModel: SettingViewModel) :SettingViewModelFactory{
        return SettingViewModelFactory(Provider { settingViewModel })
    }

}