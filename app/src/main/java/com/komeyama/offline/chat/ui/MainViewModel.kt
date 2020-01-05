package com.komeyama.offline.chat.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.communication.CommunicationContentsEntities
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val application: Application,
    private val dao: CommunicationContentsDao
): ViewModel(){

    @SuppressLint("CheckResult")
    fun test() {
        Timber.d("test main view model: " + application.toString())

        /*
        val mockEntity = CommunicationContentsEntities(0,"sender1")
        Completable.fromAction { dao.insert(mockEntity)}
            .subscribeOn(Schedulers.io())
            .subscribe()

        Observable.fromCallable{ dao.getAllCommunicationList() }
            .subscribeOn(Schedulers.io())
            .subscribe{
                Timber.d("test dao: " + it.toString())
                it.forEach{
                    Timber.d("test dao element: " + it.toString())
                }
            }
         */

    }

}