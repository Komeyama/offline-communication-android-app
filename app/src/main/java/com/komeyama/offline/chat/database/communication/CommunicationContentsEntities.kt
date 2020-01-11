package com.komeyama.offline.chat.database.communication

import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.komeyama.offline.chat.domain.CommunicationContent

@Entity(tableName = "communication_contents_entities")
data class CommunicationContentsEntities constructor(
    @PrimaryKey(autoGenerate = true)
    val contentId: Int,
    val sendUserId:String,
    val sendUserName:String,
    val receiveUserId:String,
    val receiveName:String,
    val sendTime:String,
    val content:String
)

fun List<CommunicationContentsEntities>.asDomainModels(): List<CommunicationContent> {
    return map {
        CommunicationContent(
            it.sendUserId,
            it.sendUserName,
            it.receiveUserId,
            it.receiveName,
            it.sendTime,
            it.content)
    }
}