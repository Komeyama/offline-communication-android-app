package com.komeyama.offline.chat.database.communication

import androidx.lifecycle.LiveData

object CommunicationContentsDaoTest: CommunicationContentsDao{
    override fun insert(communicationContentsEntities: CommunicationContentsEntities) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllCommunicationList(): LiveData<List<CommunicationContentsEntities>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLatestCommunication(): LiveData<CommunicationContentsEntities> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}