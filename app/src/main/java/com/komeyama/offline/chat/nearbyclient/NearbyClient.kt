package com.komeyama.offline.chat.nearbyclient

import android.app.Application
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
import javax.inject.Inject

class NearbyClient @Inject constructor(application: Application) {

    private val connectionsClient = Nearby.getConnectionsClient( application.applicationContext )
    private val serviceId = application.packageName

    fun startNearbyClient(userNameAndId: String) {
        startAdvertising(userNameAndId)
        startDiscovery()
    }

    private fun startAdvertising(userNameAndId: String) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()

        connectionsClient.startAdvertising(
            userNameAndId,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        )

    }

    private fun startDiscovery() {
        val discoveryOptions: DiscoveryOptions = DiscoveryOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            discoveryOptions
        )
    }

    private val connectionLifecycleCallback = object: ConnectionLifecycleCallback() {
        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
        }

        override fun onDisconnected(endpointId: String) {
        }

        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
        }

    }

    private val endpointDiscoveryCallback = object: EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
        }

        override fun onEndpointLost(endpointId: String) {
        }

    }

}