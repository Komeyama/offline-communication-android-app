package com.komeyama.offline.chat.ui.CommunicationHistoryList

import android.app.Application
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class CommunicationHistoryListViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    fun test() {
        Timber.d("test communication history list view model: " + application.toString())
    }

}