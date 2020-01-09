package com.komeyama.offline.chat.nearbyclient

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.asNearbyMessage
import com.squareup.moshi.Moshi
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class NearbyClient @Inject constructor(
    val application: Application,
    val moshi: Moshi
) {

    private val connectionsClient = Nearby.getConnectionsClient( application.applicationContext )
    private val serviceId = application.packageName

    private var connectedEndpointId : String = ""

    private val _aroundEndpointId: MutableLiveData<String> = MutableLiveData()
    val aroundEndpointId: LiveData<String>
        get() = _aroundEndpointId

    private val _lostEndpointId: MutableLiveData<String> = MutableLiveData()
    val lostEndpointId: LiveData<String>
        get() = _lostEndpointId

    private val _receiveContent: MutableLiveData<CommunicationContent> = MutableLiveData()
    val receiveContent: LiveData<CommunicationContent>
        get() = _receiveContent

    fun startNearbyClient(userNameAndId: String) {
        startAdvertising(userNameAndId)
        startDiscovery()
    }

    fun stopNearbyClient() {

    }

    fun acceptConnection(acceptEndpointId: String) {
        connectedEndpointId = acceptEndpointId
        connectionsClient.acceptConnection(acceptEndpointId, payloadCallback)
    }

    fun requestConnection(requestEndpointId: String, userNameAndId: String) {
        connectionsClient.requestConnection(userNameAndId, requestEndpointId, connectionLifecycleCallback).
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

    private fun startAdvertising(userNameAndId: String) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        connectionsClient.startAdvertising(
            userNameAndId,
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
            _aroundEndpointId.postValue(endpointId)
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
            _aroundEndpointId.postValue(endpointId)
        }

        override fun onEndpointLost(endpointId: String) {
            _lostEndpointId.postValue(endpointId)
        }
    }

    private val payloadCallback = object: PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receiveMessage =  payload.asBytes().toString()
            val sendMessage = moshi.adapter(NearbyCommunicationContent::class.java).fromJson(receiveMessage)
            
            sendMessage?.let {
                _receiveContent.postValue(it.asDomainModel())
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}

    }

}