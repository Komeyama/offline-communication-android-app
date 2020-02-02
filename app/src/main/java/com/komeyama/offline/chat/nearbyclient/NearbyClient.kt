package com.komeyama.offline.chat.nearbyclient

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes.*
import com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.HistoryUser
import com.komeyama.offline.chat.domain.asNearbyMessage
import com.komeyama.offline.chat.util.splitUserIdAndName
import com.komeyama.offline.chat.util.toDateString
import com.squareup.moshi.Moshi
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.util.*
import javax.inject.Inject

enum class ConnectionType{
    REQUESTER,
    RECEIVER
}

enum class RequestResult{
    NOT_REQUEST,
    LOADING,
    SUCCESS,
    CANCELED
}

enum class ConnectingStatus{
    NOT_CONNECTING,
    CONNECTING,
    LOST
}

class NearbyClient @Inject constructor(
    val application: Application,
    val moshi: Moshi
) {
    // immutable variable
    private val connectionsClient = Nearby.getConnectionsClient( application.applicationContext )
    private val serviceId = application.packageName
    private val currentActiveUsrSet: MutableSet<ActiveUser> = mutableSetOf()

    // mutable variable
    private var nearbyStrategy = P2P_POINT_TO_POINT
    private var connectionType: ConnectionType = ConnectionType.RECEIVER
    private var currentMyselfUserIdAndName : String = ""
    private var connectedEndpointId: String = ""
    private lateinit var currentOpponentInfo: HistoryUser

    // live data
    private val _aroundEndpointInfo: MutableLiveData<List<ActiveUser>> = MutableLiveData()
    val aroundEndpointInfo: LiveData<List<ActiveUser>>
        get() = _aroundEndpointInfo
    private val _inviteEndpointInfo: MutableLiveData<Iterable<*>> = MutableLiveData()
    val inviteEndpointInfo: LiveData<Iterable<*>>
        get() = _inviteEndpointInfo
    private val _receiveContent: PublishProcessor<NearbyCommunicationContent> = PublishProcessor.create()
    val receiveContent: PublishProcessor<NearbyCommunicationContent>
        get() = _receiveContent
    private val _requestResult: MutableLiveData<RequestResult> = MutableLiveData()
    val requestResult: LiveData<RequestResult>
        get() = _requestResult
    private val _connectingStatus: MutableLiveData<ConnectingStatus> = MutableLiveData()
    val connectingStatus: LiveData<ConnectingStatus>
        get() = _connectingStatus
    private val _connectedOpponentUserInfo: PublishProcessor<HistoryUser> = PublishProcessor.create()
    val connectedOpponentUserInfo: PublishProcessor<HistoryUser>
        get() = _connectedOpponentUserInfo

    fun setupNearbyClient(userIdAndName: String, nearbyStrategy: Strategy) {
        this.nearbyStrategy = nearbyStrategy
        this.currentMyselfUserIdAndName  = userIdAndName
        startNearbyClient()
    }

    fun reStartNearbyClient() {
        stopNearbyClient()
        startNearbyClient()
    }

    fun stopNearbyClient() {
        resetCommunication()
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
    }

    fun finishCommunication() {
        connectionsClient.stopAllEndpoints()
        _connectingStatus.postValue(ConnectingStatus.NOT_CONNECTING)
        _connectedOpponentUserInfo.onNext(currentOpponentInfo)
        resetCommunication()
    }

    fun acceptConnection(acceptEndpointId: String) {
        Timber.d("acceptConnection: %s", acceptEndpointId)
        _connectingStatus.postValue(ConnectingStatus.CONNECTING)
        connectedEndpointId = acceptEndpointId
        connectionsClient.acceptConnection(acceptEndpointId, payloadCallback)
    }

    fun rejectConnection(rejectEndpointId: String) {
        Timber.d("rejectConnection: %s", rejectEndpointId)
        connectionsClient.rejectConnection(rejectEndpointId)
    }

    fun requestConnection(userIdAndName: String, requestEndpointId: String) {
        connectionType = ConnectionType.REQUESTER
        _requestResult.postValue(RequestResult.LOADING)
        connectionsClient.requestConnection(userIdAndName, requestEndpointId, connectionLifecycleCallback).
            addOnSuccessListener {
                Timber.d("success requestConnection! :%s", requestEndpointId)
                connectionsClient.acceptConnection(requestEndpointId, payloadCallback)
                _requestResult.postValue(RequestResult.SUCCESS)
                _connectingStatus.postValue(ConnectingStatus.CONNECTING)
            }.addOnFailureListener {
                Timber.d("failure requestConnection!")
                _requestResult.postValue(RequestResult.CANCELED)
                reStartNearbyClient()
            /**
             * Todo: Add failure message trigger
             */
        }
    }

    fun sendPayload(communicationContent: CommunicationContent) {
        val payLoad = createSendPayload(communicationContent)
        Timber.d("sendPayload endpointid %s",connectedEndpointId)
        connectionsClient.sendPayload(connectedEndpointId, payLoad)
    }

    private fun startNearbyClient() {
        startAdvertising()
        startDiscovery()
    }

    private fun startAdvertising() {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder().setStrategy(nearbyStrategy).build()
        connectionsClient.startAdvertising(
            currentMyselfUserIdAndName,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Timber.d("success startAdvertising!")
        }.addOnFailureListener {
            Timber.d("failure startAdvertising!")
            /**
             * Todo: Add retry process
             */
        }
    }

    private fun startDiscovery() {
        val discoveryOptions: DiscoveryOptions = DiscoveryOptions.Builder().setStrategy(nearbyStrategy).build()
        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            discoveryOptions
        ).addOnSuccessListener {
            Timber.d("success startDiscovery!")
        }.addOnFailureListener {
            Timber.d("failure startDiscovery!")
            /**
             * Todo: Add retry process
             */
        }
    }

    private val connectionLifecycleCallback = object: ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            Timber.d("onConnectionInitiated: %s", connectionInfo)
            connectedEndpointId = endpointId
            val list:Iterable<*> = listOf(
                ActiveUser(
                    connectionInfo.endpointName.splitUserIdAndName().userId,
                    connectionInfo.endpointName.splitUserIdAndName().userName,
                    endpointId
                ),
                connectionType)
            _inviteEndpointInfo.postValue(list)

            currentOpponentInfo = HistoryUser(
                connectionInfo.endpointName.splitUserIdAndName().userId,
                connectionInfo.endpointName.splitUserIdAndName().userName,
                Date().toDateString()
            )
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            Timber.d("connection result: %s", result.status)
            when(result.status.statusCode) {
                STATUS_OK-> {
                    connectedEndpointId = endpointId
                }
                else -> {
                    _connectingStatus.postValue(ConnectingStatus.LOST)
                    _connectedOpponentUserInfo.onNext(currentOpponentInfo)
                    resetCommunication()
                }

            }
        }

        override fun onDisconnected(endpointId: String) {
            Timber.d("onDisconnected: %s", endpointId)
            _connectingStatus.postValue(ConnectingStatus.LOST)
            _connectedOpponentUserInfo.onNext(currentOpponentInfo)
            resetCommunication()
        }
    }

    private val endpointDiscoveryCallback = object: EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.d("info: %s", info)
            currentActiveUsrSet.add(
                ActiveUser(
                    info.endpointName.splitUserIdAndName().userId,
                    info.endpointName.splitUserIdAndName().userName,
                    endpointId)
            )
            _aroundEndpointInfo.postValue(currentActiveUsrSet.toList())
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.d("onEndpointLost: %s", endpointId)
            var lostUserId = ""
            var lostUserName = ""
            currentActiveUsrSet.forEach{
                if (it.endPointId.equals(endpointId)) {
                    lostUserId = it.id
                    lostUserName = it.name
                }
            }
            currentActiveUsrSet.remove(ActiveUser(lostUserId,lostUserName,endpointId))
            _aroundEndpointInfo.postValue(currentActiveUsrSet.toList())
        }
    }

    private val payloadCallback = object: PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Timber.d("onPayloadReceived: %s", endpointId)
            val receiveNearbyCommunicationContent = createReceiveNearbyCommunicationContent(payload)
            receiveNearbyCommunicationContent?.let {
                _receiveContent.onNext(it)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Timber.d("onPayloadTransferUpdate: %s", endpointId)
            connectedEndpointId = endpointId
            currentOpponentInfo.latestDate = Date().toDateString()
        }

    }

    private fun createSendPayload(communicationContent: CommunicationContent): Payload {
        val sendMessage = moshi.adapter(NearbyCommunicationContent::class.java).toJson(communicationContent.asNearbyMessage())
        return Payload.fromBytes(sendMessage.toString().toByteArray(StandardCharsets.UTF_8))
    }

    private fun createReceiveNearbyCommunicationContent(payload: Payload): NearbyCommunicationContent? {
        val receiveMessage = payload.asBytes()?.let { String(it) } ?: ""
        return moshi.adapter(NearbyCommunicationContent::class.java).fromJson(receiveMessage)
    }

    private fun resetCommunication() {
        Timber.d("resetCommunication: ")
        connectionType = ConnectionType.RECEIVER
        _requestResult.postValue(RequestResult.NOT_REQUEST)
        val list:Iterable<*> = listOf(ActiveUser.EMPTY, connectionType)
        _inviteEndpointInfo.postValue(list)
    }
}
