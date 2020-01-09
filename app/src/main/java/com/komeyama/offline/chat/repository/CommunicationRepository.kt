package com.komeyama.offline.chat.repository

import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import javax.inject.Inject

class CommunicationRepository @Inject constructor(
    val dao :CommunicationContentsDao,
    val nearbyClient: NearbyClient
) {

    fun test() {
        dao.getAllCommunicationList()
    }

}