package com.komeyama.offline.chat.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListViewModel
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListViewModel
import com.komeyama.offline.chat.ui.MainViewModel
import com.komeyama.offline.chat.ui.setting.SettingViewModel
import javax.inject.Inject
import javax.inject.Provider

class MainViewModelFactory @Inject constructor(
    private val provider: Provider<MainViewModel>
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}

class CommunicableUserListViewModelFactory @Inject constructor(
    private val provider: Provider<CommunicableUserListViewModel>
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}

class CommunicationHistoryListViewModelFactory @Inject constructor(
    private val provider: Provider<CommunicationHistoryListViewModel>
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}

class SettingViewModelFactory @Inject constructor(
    private val provider: Provider<SettingViewModel>
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}
