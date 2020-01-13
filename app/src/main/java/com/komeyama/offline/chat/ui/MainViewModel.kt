package com.komeyama.offline.chat.ui

import androidx.lifecycle.ViewModel
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.repository.CommunicationRepository
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val communicationContentsDao: CommunicationContentsDao,
    private val userInformationDao: UserInformationDao,
    private val communicationRepository: CommunicationRepository,
    private val nearbyClient: NearbyClient
): ViewModel(){

    val activeUserList = nearbyClient.aroundEndpointInfo

    val requestedUser = nearbyClient.requestedEndpointInfo

    fun startNearbyClient() {
        Timber.d("start nearby client")
        nearbyClient.startNearbyClient("")
    }

    fun acceptConnection(acceptEndpointId: String) {
        nearbyClient.acceptConnection(acceptEndpointId)
    }

    fun rejectConnection(rejectEndpointId: String) {
        nearbyClient.rejectConnection(rejectEndpointId)
    }

    override fun onCleared() {
        nearbyClient.stopNearbyClient()
        super.onCleared()
    }

}