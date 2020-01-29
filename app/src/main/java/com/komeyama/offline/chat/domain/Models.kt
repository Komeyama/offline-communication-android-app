package com.komeyama.offline.chat.domain

import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent
import com.komeyama.offline.chat.util.toDateString
import java.util.*

data class ActiveUser(val id:String,
                      val name:String,
                      val endPointId: String)

data class HistoryUser(val id:String,
                      val name:String)

data class CommunicationContent(val sendUserId:String,
                                val sendUserName:String,
                                val receiveUserId:String,
                                val receiveName:String,
                                val sendTime: Date,
                                val content:String)

fun CommunicationContent.asNearbyMessage(): NearbyCommunicationContent {
    return NearbyCommunicationContent(
        sendUserId,
        sendUserName,
        receiveUserId,
        receiveName,
        sendTime.toDateString(),
        content)
}

