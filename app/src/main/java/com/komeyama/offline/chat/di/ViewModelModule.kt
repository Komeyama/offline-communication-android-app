package com.komeyama.offline.chat.di

import com.komeyama.offline.chat.ui.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class ViewModelModule {
    @Provides
    fun provideMainViewModelFactory(mainViewModel: MainViewModel):MainViewModelFactory {
        return MainViewModelFactory(Provider { mainViewModel })
    }
}