package com.komeyama.offline.chat.database.communication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "communication_contents_entities")
data class CommunicationContentsEntities constructor(
    @PrimaryKey(autoGenerate = true)
    val communicationId: Int,
    val senderId: String,
    val senderName: String,
    val receiverId: String,
    val contents: String,
    val roomId: String
)