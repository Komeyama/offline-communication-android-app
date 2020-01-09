package com.komeyama.offline.chat.domain

import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent

data class ActiveUser(val id:String,
                      val name:String)

data class CommunicationContent(val sendUserId:String,
                                val sendUserName:String,
                                val receiveUserId:String,
                                val receiveName:String,
                                val sendTime:String,
                                val content:String)

fun CommunicationContent.asNearbyMessage(endPointId: String): NearbyCommunicationContent {
    return NearbyCommunicationContent(
        sendUserId,
        sendUserName,
        receiveUserId,
        receiveName,
        sendTime,
        endPointId,
        content)
}