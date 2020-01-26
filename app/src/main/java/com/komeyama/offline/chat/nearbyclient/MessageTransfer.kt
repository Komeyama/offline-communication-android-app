package com.komeyama.offline.chat.nearbyclient

import com.komeyama.offline.chat.database.communication.CommunicationContentsEntities
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NearbyCommunicationContent(
    val sendUserId:String,
    val sendUserName:String,
    val receiveUserId:String,
    val receiveName:String,
    val sendTime: String,
    val content:String)

fun NearbyCommunicationContent.asDomainModel(): CommunicationContentsEntities {
    return CommunicationContentsEntities(
        0,
        sendUserId,
        sendUserName,
        receiveUserId,
        receiveName,
        sendTime,
        content)
}