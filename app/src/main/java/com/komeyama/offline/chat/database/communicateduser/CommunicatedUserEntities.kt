package com.komeyama.offline.chat.database.communicateduser

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.komeyama.offline.chat.domain.HistoryUser

@Entity(tableName = "communicated_user_entities")
data class CommunicatedUserEntities (
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val communicatedUserId: String = "",
    val communicatedUserName: String,
    val latestDate: String
)

fun List<CommunicatedUserEntities>.asDomainModels(): List<HistoryUser> {
    return map {
        HistoryUser(
            it.communicatedUserId,
            it.communicatedUserName,
            it.latestDate
        )
    }
}