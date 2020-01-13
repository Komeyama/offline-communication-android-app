package com.komeyama.offline.chat.nearbyclient

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.asNearbyMessage
import com.komeyama.offline.chat.util.splitUserIdAndName
import com.squareup.moshi.Moshi
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class NearbyClient @Inject constructor(
    val application: Application,
    val moshi: Moshi
) {

    private val connectionsClient = Nearby.getConnectionsClient( application.applicationContext )
    private val serviceId = application.packageName

    private var connectedEndpointId: String = ""
    private val currentActiveUsrList: MutableList<ActiveUser> = mutableListOf()

    private val _aroundEndpointInfo: MutableLiveData<List<ActiveUser>> = MutableLiveData()
    val aroundEndpointInfo: LiveData<List<ActiveUser>>
        get() = _aroundEndpointInfo

    private val _lostEndpointId: PublishProcessor<String> = PublishProcessor.create()
    val lostEndpointId: PublishProcessor<String>
        get() = _lostEndpointId

    private val _requestedEndpointInfo: MutableLiveData<ActiveUser> = MutableLiveData()
    val requestedEndpointInfo: LiveData<ActiveUser>
        get() = _requestedEndpointInfo

    private val _receiveContent: PublishProcessor<NearbyCommunicationContent> = PublishProcessor.create()
    val receiveContent: PublishProcessor<NearbyCommunicationContent>
        get() = _receiveContent

    fun startNearbyClient(userIdAndName: String) {
        startAdvertising(userIdAndName)
        startDiscovery()
    }

    fun stopNearbyClient() {
        /*

        add nearby stop process

         */
    }

    fun acceptConnection(acceptEndpointId: String) {
        Timber.d("acceptConnection: %s", acceptEndpointId)
        connectedEndpointId = acceptEndpointId
        connectionsClient.acceptConnection(acceptEndpointId, payloadCallback)
    }

    fun rejectConnection(rejectEndpointId: String) {
        Timber.d("rejectConnection: %s", rejectEndpointId)
        connectionsClient.rejectConnection(rejectEndpointId)
    }

    fun requestConnection(requestEndpointId: String, userIdAndName: String) {
        connectionsClient.requestConnection(userIdAndName, requestEndpointId, connectionLifecycleCallback).
            addOnSuccessListener {
                Timber.d("success requestConnection!")
            }.addOnFailureListener {
                Timber.d("failure requestConnection!")
                /*

                 Add retry process

                 */
            }
    }

    fun sendPayload(communicationContent: CommunicationContent) {
        val sendMessage = moshi.adapter(NearbyCommunicationContent::class.java).
            toJson(communicationContent.asNearbyMessage(connectedEndpointId))

        sendMessage?.let {
            connectionsClient.sendPayload(
                connectedEndpointId,
                Payload.fromBytes(sendMessage.toString().toByteArray(StandardCharsets.UTF_8)))
        }
    }

    private fun startAdvertising(userIdAndName: String) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        connectionsClient.startAdvertising(
            userIdAndName,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Timber.d("success startAdvertising!")
        }.addOnFailureListener {
            Timber.d("failure startAdvertising!")
            /*

             Add retry process

             */
        }
    }

    private fun startDiscovery() {
        val discoveryOptions: DiscoveryOptions = DiscoveryOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            discoveryOptions
        ).addOnSuccessListener {
            Timber.d("success startDiscovery!")
        }.addOnFailureListener {
            Timber.d("failure startDiscovery!")
            /*

             Add retry process

             */
        }
    }

    private val connectionLifecycleCallback = object: ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            _requestedEndpointInfo.postValue(
                ActiveUser(
                    connectionInfo.endpointName.splitUserIdAndName().userId,
                    connectionInfo.endpointName.splitUserIdAndName().userName,
                    endpointId
                )
            )
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when(result.status) {
                Status.RESULT_SUCCESS -> {
                    connectedEndpointId = endpointId
                }
                Status.RESULT_CANCELED -> {}
                Status.RESULT_INTERRUPTED -> {}
            }
        }

        override fun onDisconnected(endpointId: String) {}
    }

    private val endpointDiscoveryCallback = object: EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            currentActiveUsrList.remove(
                ActiveUser(
                    info.endpointName.splitUserIdAndName().userId,
                    info.endpointName.splitUserIdAndName().userName,
                    endpointId)
            )
            _aroundEndpointInfo.postValue(currentActiveUsrList)
        }

        override fun onEndpointLost(endpointId: String) {
            _lostEndpointId.onNext(endpointId)
        }
    }

    private val payloadCallback = object: PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receiveMessage =  payload.asBytes().toString()
            val sendMessage = moshi.adapter(NearbyCommunicationContent::class.java).fromJson(receiveMessage)

            sendMessage?.let {
                _receiveContent.onNext(it)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}

    }

}