package com.komeyama.offline.chat.nearbyclient

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.nearby.connection.Payload
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.domain.CommunicationContent
import com.komeyama.offline.chat.domain.asNearbyMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class NearbyClientTest {

    lateinit var nearbyClient: NearbyClient
    lateinit var moshi: Moshi
    private lateinit var aroundEndpointInfo: MutableLiveData<List<ActiveUser>>
    private lateinit var inviteEndpointInfo: MutableLiveData<ActiveUser>

    @Before
    fun setUp() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        nearbyClient = NearbyClient(ApplicationProvider.getApplicationContext(), moshi)

        val aroundEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_aroundEndpointInfo")
        aroundEndpointInfoFiled.isAccessible = true
        aroundEndpointInfo = aroundEndpointInfoFiled.get(nearbyClient) as MutableLiveData<List<ActiveUser>>

        val inviteEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_inviteEndpointInfo")
        inviteEndpointInfoFiled.isAccessible = true
        inviteEndpointInfo = inviteEndpointInfoFiled.get(nearbyClient) as MutableLiveData<ActiveUser>
    }

    @Test
    fun aroundEndpointInfoTest() {
        val currentActiveUsrList: MutableList<ActiveUser> = mutableListOf()
        currentActiveUsrList.add(
            ActiveUser(
                "01234abcde",
                "name 01",
                "endpointId01")
        )

        currentActiveUsrList.add(
            ActiveUser(
                "56789fghi",
                "name 03",
                "endpointId02")
        )

        aroundEndpointInfo.postValue(currentActiveUsrList)

        val info = aroundEndpointInfo.value
        info?.apply {
            assertEquals(this.get(0),
                ActiveUser(
                    "01234abcde",
                    "name 01",
                    "endpointId01")
            )
            assertEquals(this.get(1),
                ActiveUser(
                    "56789fghi",
                    "name 03",
                    "endpointId02")
            )
        }
    }

    @Test
    fun requestedEndpointInfoTest() {
        nearbyClient.acceptConnection("")
        inviteEndpointInfo.postValue(
            ActiveUser("abc","def","ghi")
        )
        val id = inviteEndpointInfo.value?.id ?: ""
        val name = inviteEndpointInfo.value?.name ?: ""
        val endPointId = inviteEndpointInfo.value?.endPointId ?: ""
        assertEquals(id,"abc")
        assertEquals(name,"def")
        assertEquals(endPointId,"ghi")
    }

    @Test
    fun communicationDataTest() {
        val communicationContent = CommunicationContent(
            "abc",
            "user name",
            "receiveId",
            "receive name",
            "2020-01-31 00:11:22",
            "hello world")

        val createSendPayload = NearbyClient::class.java.getDeclaredMethod("createSendPayload", CommunicationContent::class.java, String::class.java)
        createSendPayload.isAccessible = true
        val payload = createSendPayload.invoke(nearbyClient, communicationContent, "endpointId")
        val createReceiveNearbyCommunicationContent = NearbyClient::class.java.getDeclaredMethod("createReceiveNearbyCommunicationContent", Payload::class.java)
        createReceiveNearbyCommunicationContent.isAccessible = true
        assertEquals(
            createReceiveNearbyCommunicationContent.invoke(nearbyClient, payload),
            communicationContent.asNearbyMessage("endpointId")
        )
    }

}