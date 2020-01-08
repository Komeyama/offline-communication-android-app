package com.komeyama.offline.chat.repository

import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.service.NearbyService
import javax.inject.Inject

class CommunicationRepository @Inject constructor(
    val dao :CommunicationContentsDao,
    val nearbyService: NearbyService
) {

    fun test() {
        dao.getAllCommunicationList()
    }

}