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
    suspend fun checkUserName() {
        withContext(Dispatchers.IO) {
            disposable = nearbyClient.
                connectedOpponentUserInfo.
                observeOn(Schedulers.io()).
                subscribe { newHistoryUser ->
                    Timber.d("new communicated user: %s", newHistoryUser)
                    val currentCommunicatedIds: MutableSet<String> = mutableSetOf()
                    dao.getCommunicatedUserList().map {
                        currentCommunicatedIds.add(it.communicatedUserId)
                    }

                    if (!currentCommunicatedIds.contains(newHistoryUser.id) || currentCommunicatedIds.isEmpty()) {
                        Timber.d("old communicated user id != old communicated user id")
                        dao.insert(newHistoryUser.asDomainModel())
                    } else {
                        Timber.d("old communicated user id == old communicated user id")
                        dao.updateDate(newHistoryUser.id, newHistoryUser.latestDate)
                    }
            }
        }
    }

    fun stopCheckUserName() {
        disposable?.dispose()
    }
}