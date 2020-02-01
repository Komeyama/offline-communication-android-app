package com.komeyama.offline.chat.ui

import androidx.lifecycle.*
import com.google.android.gms.nearby.connection.Strategy
import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.asNearbyMessage
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent
import com.komeyama.offline.chat.repository.CommunicatedUserRepository
import com.komeyama.offline.chat.repository.CommunicationRepository
import com.komeyama.offline.chat.service.UserInformationService
import com.komeyama.offline.chat.util.createUserIdAndName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userInformationService: UserInformationService,
    private val communicationRepository: CommunicationRepository,
    private val communicatedUserRepository: CommunicatedUserRepository,
    private val nearbyClient: NearbyClient
): ViewModel(){

    val activeUserList = nearbyClient.aroundEndpointInfo
    val invitedInfo = nearbyClient.inviteEndpointInfo
    val requestResult = nearbyClient.requestResult
    val connectingStatus = nearbyClient.connectingStatus
    val communicatedList = communicatedUserRepository.communicatedList
    private val _isExistUserInformation: MutableLiveData<Boolean> = MutableLiveData()
    val isExistUserInformation:LiveData<Boolean>
        get() = _isExistUserInformation

    val isCloseDialog: MutableLiveData<Boolean> = MutableLiveData()
    val nameText: MutableLiveData<String> = MutableLiveData()
    lateinit var currentUserInformation: UserInformationEntities

    init {
        hasUserInformation()
    }

    private suspend fun startNearbyClient() {
        withContext(Dispatchers.IO) {
            nearbyClient.setupNearbyClient(currentUserInformation.createUserIdAndName(), Strategy.P2P_POINT_TO_POINT)
        }
    }

    private suspend fun stopNearbyClient() {
        withContext(Dispatchers.IO) {
            nearbyClient.stopNearbyClient()
        }
    }

    private fun hasUserInformation() {
        viewModelScope.launch {
            val isExist = userInformationService.existsUserInformation()
            _isExistUserInformation.postValue(isExist)

            if (isExist) {
                currentUserInformation = userInformationService.getUserInformation()
                startNearbyClient()
            }
        }
    }

    private fun createUserInformation(userName: String) {
        viewModelScope.launch {
            try {
                userInformationService.insertUserInformation(userName)
                currentUserInformation = userInformationService.getUserInformation()
                startNearbyClient()
            } catch (error: IOException) {}
        }
    }

    fun selectUserContent(communicationOpponentId: String): LiveData<List<NearbyCommunicationContent>> =
        Transformations.switchMap(communicationRepository.communicationContents){ list ->
            val contentList = mutableListOf<NearbyCommunicationContent>()
            val selectedContents: MutableLiveData<List<NearbyCommunicationContent>> = MutableLiveData()
            list.forEach{
                if (it.sendUserId == communicationOpponentId || it.receiveUserId == communicationOpponentId) {
                    contentList.add(it)
                }
            }
            selectedContents.value = contentList
            return@switchMap selectedContents
        }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch {
            try {
                val oldUserInformation = userInformationService.getUserInformation()
                userInformationService.updateUserInformation(oldUserInformation.copy(userName = newUserName))
                currentUserInformation = userInformationService.getUserInformation()
                stopNearbyClient()
                startNearbyClient()
            } catch (error: IOException) {}
        }
    }

    fun setUserName() {
        /**
         * Todo: add "text validation"
         */
        Timber.d("set user name! %s", nameText.value.toString())
        createUserInformation(nameText.value.toString())
        isCloseDialog.postValue(true)
    }

    fun acceptConnection(acceptEndpointId: String) {
        nearbyClient.acceptConnection(acceptEndpointId)
    }

    fun rejectConnection(rejectEndpointId: String) {
        nearbyClient.rejectConnection(rejectEndpointId)
    }

    fun requestConnection(requestEndpointId: String) {
        nearbyClient.requestConnection( currentUserInformation.createUserIdAndName(), requestEndpointId)
    }

    fun checkCommunicatedUserName() {
        Timber.d("checkCommunicatedUserName")
        viewModelScope.launch {
            try {
                communicatedUserRepository.checkUserName()
            } catch (error: IOException) {}
        }
    }

    fun stopCheckCommunicatedUserName() {
        communicatedUserRepository.stopCheckUserName()
    }

    fun startRefreshMessages() {
        communicationRepository.refreshMessages()
    }

    fun stopRefreshMessages(){
        communicationRepository.stopRefreshMessages()
    }

    fun sendMessage(message: String, communicationOpponentId: String, communicationOpponentName: String) {
        val communicationContent = createCommunicationContent(message,communicationOpponentId,communicationOpponentName)
        viewModelScope.launch {
            try {
                communicationRepository.updateUserContent(communicationContent.asNearbyMessage())
            } catch (error: IOException) {}
        }
        nearbyClient.sendPayload(communicationContent)
    }

    private fun createCommunicationContent(message: String, communicationOpponentId: String, communicationOpponentName: String): CommunicationContent {
        return CommunicationContent(
            currentUserInformation.userId,
            currentUserInformation.userName,
            communicationOpponentId,
            communicationOpponentName,
            Date(),
            message
        )
    }

    override fun onCleared() {
        nearbyClient.stopNearbyClient()
        super.onCleared()
    }

}