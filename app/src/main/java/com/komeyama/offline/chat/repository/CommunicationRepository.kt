package com.komeyama.offline.chat.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.communication.asDomainModels
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent
import com.komeyama.offline.chat.nearbyclient.asDomainModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CommunicationRepository @Inject constructor(
    val dao :CommunicationContentsDao,
    val nearbyClient: NearbyClient
) {

    val communicationContents: LiveData<List<NearbyCommunicationContent>> = Transformations.map(dao.getAllCommunicationList()){
        it.asDomainModels()
    }
    lateinit var disposable:Disposable

    suspend fun updateUserContent(nearbyCommunicationContent:NearbyCommunicationContent) = withContext(Dispatchers.IO) {
        dao.insert(nearbyCommunicationContent.asDomainModel())
    }

    @SuppressLint("CheckResult")
    fun refreshMessages() {
        disposable = nearbyClient.
            receiveContent.
            observeOn(Schedulers.io()).
            subscribe {
                Timber.d("receive message: %s", it.content)
                dao.insert(it.asDomainModel())
            }
    }

    fun stopRefreshMessages() {
        disposable.dispose()
    }

}