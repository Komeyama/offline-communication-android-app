package com.komeyama.offline.chat.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.komeyama.offline.chat.database.communicateduser.CommunicatedUserDao
import com.komeyama.offline.chat.database.communicateduser.CommunicatedUserEntities
import com.komeyama.offline.chat.database.communicateduser.asDomainModels
import com.komeyama.offline.chat.domain.HistoryUser
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.nearbyclient.asDomainModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CommunicatedUserRepository (
    val dao:CommunicatedUserDao,
    val nearbyClient: NearbyClient
) {
    var disposable: Disposable? = null
    val communicatedList: LiveData<List<HistoryUser>> = Transformations.map(dao.getCommunicatedUserListLiveData()){
        it.asDomainModels()
    }

    suspend fun getCommunicatedUserList(): List<CommunicatedUserEntities> = withContext(Dispatchers.IO) {
        dao.getCommunicatedUserList()
    }

    @SuppressLint("CheckResult")
    suspend fun checkUserName(currentCommunicatedList: List<CommunicatedUserEntities>) {
        withContext(Dispatchers.IO) {
            disposable = nearbyClient.
                connectedOpponentUserInfo.
                observeOn(Schedulers.io()).
                subscribe { newHistoryUser ->
                    Timber.d("new communicated user: %s", newHistoryUser)
                    if (currentCommunicatedList.isEmpty()) {
                        Timber.d("old communicated user is null")
                        dao.insert(newHistoryUser.asDomainModel())
                    }
                    currentCommunicatedList.forEach { oldHistoryUser ->
                        if(oldHistoryUser.communicatedUserId != newHistoryUser.id) {
                            Timber.d("old communicated user id != old communicated user id")
                            dao.insert(newHistoryUser.asDomainModel())
                        } else {
                            Timber.d("old communicated user id == old communicated user id")
                            dao.update(newHistoryUser.asDomainModel())
                    }
                }
            }
        }
    }

    fun stopCheckUserName() {
        disposable?.dispose()
    }
}