package com.komeyama.offline.chat.di

import com.komeyama.offline.chat.ui.MainViewModel
import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListViewModel
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListViewModel
import com.komeyama.offline.chat.ui.setting.SettingViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class ViewModelModule {
    @Provides
    fun provideMainViewModelFactory(mainViewModel: MainViewModel):MainViewModelFactory {
        return MainViewModelFactory(Provider { mainViewModel })
    }

    @Provides
    fun provideCommunicableUserListViewModelFactory(communicableUserListViewModel: CommunicableUserListViewModel) :CommunicableUserListViewModelFactory{
        return CommunicableUserListViewModelFactory(Provider { communicableUserListViewModel })
    }

    @Provides
    fun provideCommunicationHistoryListViewModelFactory(communicationHistoryListViewModel: CommunicationHistoryListViewModel) :CommunicationHistoryListViewModelFactory{
        return CommunicationHistoryListViewModelFactory(Provider { communicationHistoryListViewModel })
    }

    @Provides
    fun provideSettingViewModelFactory(settingViewModel: SettingViewModel) :SettingViewModelFactory{
        return SettingViewModelFactory(Provider { settingViewModel })
    }
}