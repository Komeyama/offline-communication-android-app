package com.komeyama.offline.chat.nearbyclient

import com.komeyama.offline.chat.domain.CommunicationContent
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NearbyCommunicationContent(
    val sendUserId:String,
    val sendUserName:String,
    val receiveUserId:String,
    val receiveName:String,
    val sendTime:String,
    val endpointId:String,
    val content:String)

fun NearbyCommunicationContent.asDomainModel(): CommunicationContent {
    return CommunicationContent(
        sendUserId,
        sendUserName,
        receiveUserId,
        receiveName,
        sendTime,
        content)
}