package com.komeyama.offline.chat.ui.Setting

import android.app.Application
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    fun test() {
        Timber.d("test setting view model: " + application.toString())
    }

}