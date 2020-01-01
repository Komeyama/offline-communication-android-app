package com.komeyama.offline.chat.ui.CommunicableUserList

import android.app.Application
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class CommunicableUserListViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    fun test() {
        Timber.d("test communicable user list view model: " + application.toString())
    }

}