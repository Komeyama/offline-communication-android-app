package com.komeyama.offline.chat.ui

import androidx.lifecycle.*
import com.komeyama.offline.chat.database.communication.CommunicationContentsDao
import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent
import com.komeyama.offline.chat.repository.CommunicationRepository
import com.komeyama.offline.chat.service.UserInformationService
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val communicationContentsDao: CommunicationContentsDao,
    private val userInformationDao: UserInformationDao,
    private val userInformationService: UserInformationService,
    private val communicationRepository: CommunicationRepository,
    private val nearbyClient: NearbyClient
): ViewModel(){

    val activeUserList = nearbyClient.aroundEndpointInfo
    val invitedInfo = nearbyClient.inviteEndpointInfo
    val requestResult = nearbyClient.requestResult
    private val _isExistUserInformation: MutableLiveData<Boolean> = MutableLiveData()
    val isExistUserInformation:LiveData<Boolean>
        get() = _isExistUserInformation

    val isCloseDialog: MutableLiveData<Boolean> = MutableLiveData()
    val nameText: MutableLiveData<String> = MutableLiveData()

    init {
        hasUserInformation()
    }

    fun selectedUserContent(communicationOpponentId: String): LiveData<List<NearbyCommunicationContent>> =
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

    fun startNearbyClient() {
        Timber.d("start nearby client")
        /**
         * Todo: change "userIdAndName" to registered name and id
         */
        nearbyClient.startNearbyClient("")

    }

    private fun hasUserInformation() {
        viewModelScope.launch {
            val isExist = userInformationService.existsUserInformation()
            _isExistUserInformation.postValue(isExist)
        }
    }

    fun createUserInformation(userName: String) {
        viewModelScope.launch {
            try {
                userInformationService.insertUserInformation(userName)
            } catch (error: IOException) {}
        }
    }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch {
            try {
                val oldUserInformation = userInformationService.getUserInformation()
                userInformationService.updateUserInformation(oldUserInformation[0].copy(userName = newUserName))
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
        /**
         * Todo: change "userIdAndName" to registered name and id
         */
        nearbyClient.requestConnection("userIdAndName", requestEndpointId)
    }

    fun startRefreshMessages() {
        communicationRepository.refreshMessages()
    }

    override fun onCleared() {
        nearbyClient.stopNearbyClient()
        super.onCleared()
    }

}