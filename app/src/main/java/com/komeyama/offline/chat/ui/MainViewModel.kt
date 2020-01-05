package com.komeyama.offline.chat.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.communication.CommunicationContentsEntities
import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val application: Application,
    private val communicationContentsDao: CommunicationContentsDao,
    private val userInformationDao: UserInformationDao
): ViewModel(){

    @SuppressLint("CheckResult")
    fun test() {
        Timber.d("test main view model: " + application.toString())

        /*
        val mockEntity = CommunicationContentsEntities(
            0,
            "senderId1",
            "senderName1",
            "receiverId1",
            "receiverName1",
            "roomID#1")
        Completable.fromAction { communicationContentsDao.insert(mockEntity)}
            .subscribeOn(Schedulers.io())
            .subscribe()

        Observable.fromCallable{ communicationContentsDao.getAllCommunicationList() }
            .subscribeOn(Schedulers.io())
            .subscribe{
                Timber.d("test communication dao: " + it.toString())
                it.forEach{
                    Timber.d("test communication dao element: " + it.toString())
                }
            }

//        val mockEntityUser = UserInformationEntities(
//            "MyId",
//            "MyName")
//        Completable.fromAction { userInformationDao.insert(mockEntityUser)}
//            .subscribeOn(Schedulers.io())
//            .subscribe()

        Observable.fromCallable{ userInformationDao.getUserInformation() }
            .subscribeOn(Schedulers.io())
            .subscribe{
                Timber.d("test user dao: " + it.toString())
                it.forEach{
                    Timber.d("test user dao element: " + it.toString())
                }
            }
         */

    }

}