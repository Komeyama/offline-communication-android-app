package com.komeyama.offline.chat.database.communication

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent

@Entity(tableName = "communication_contents_entities")
data class CommunicationContentsEntities constructor(
    @PrimaryKey(autoGenerate = true)
    val contentId: Int,
    val sendUserId:String,
    val sendUserName:String,
    val receiveUserId:String,
    val receiveName:String,
    val sendTime:String,
    val endPointId:String,
    val content:String
)

fun List<CommunicationContentsEntities>.asDomainModels(): List<NearbyCommunicationContent> {
    return map {
        NearbyCommunicationContent(
            it.sendUserId,
            it.sendUserName,
            it.receiveUserId,
            it.receiveName,
            it.sendTime,
            it.endPointId,
            it.content)
    }
}