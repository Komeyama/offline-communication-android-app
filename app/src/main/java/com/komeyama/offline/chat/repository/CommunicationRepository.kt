package com.komeyama.offline.chat.repository

import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import javax.inject.Inject

class CommunicationRepository @Inject constructor(val dao :CommunicationContentsDao) {

    fun test() {
        dao.getAllCommunicationList()
    }

}