package com.komeyama.offline.chat.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val communicationContentsDao: CommunicationContentsDao,
    private val userInformationDao: UserInformationDao,
    private val nearbyClient: NearbyClient
): ViewModel(){

    fun startNearbyClient() {
        Timber.d("start services")
        nearbyClient.startNearbyClient("")
    }

    @SuppressLint("CheckResult")
    fun test() {
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