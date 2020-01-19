package com.komeyama.offline.chat.util

import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import java.text.SimpleDateFormat
import java.util.*

fun String.splitUserIdAndName(): UserInformationEntities {
    val indexOfColon = this.indexOf(":")
    val userId = this.substring(0, indexOfColon)
    val userName = this.substring(indexOfColon + 1, this.length)
    return UserInformationEntities(userId = userId, userName = userName)
}

const val dateFormatType = "yyyy/MM/dd HH:mm:ss"

fun String.toDate(): Date{
    return SimpleDateFormat(dateFormatType).parse(this)
}

fun Date.toDateString(): String{
    return SimpleDateFormat(dateFormatType).format(this)
}

enum class RequestResult{
    LOADING,
    SUCCESS,
    CANCELED,
    INTERRUPTED
}