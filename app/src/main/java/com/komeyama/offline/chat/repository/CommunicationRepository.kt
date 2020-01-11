package com.komeyama.offline.chat.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.communication.asDomainModels
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.asDomainModel
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommunicationRepository @Inject constructor(
    val dao :CommunicationContentsDao,
    val nearbyClient: NearbyClient
) {

    val communicationContents: LiveData<List<CommunicationContent>> = Transformations.map(dao.getAllCommunicationList()){
        it.asDomainModels()
    }

    @SuppressLint("CheckResult")
    fun reciveNearybyMessage() {
        nearbyClient.
            receiveContent.
            subscribeOn(Schedulers.io()).
            subscribe {
                dao.insert(it.asDomainModel())
            }
    }

}