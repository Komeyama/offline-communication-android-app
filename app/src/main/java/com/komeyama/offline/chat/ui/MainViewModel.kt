package com.komeyama.offline.chat.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val application: Application
): ViewModel(){

    fun test() {
        Timber.d("test main view model: " + application.toString())
    }

}