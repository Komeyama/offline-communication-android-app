package com.komeyama.offline.chat.util

import java.text.SimpleDateFormat
import java.util.*

data class UserIdAndName(
    val userId: String,
    val userName: String
)

fun String.splitUserIdAndName(): UserIdAndName{
    val indexOfColon = this.indexOf(":")
    val userId = this.substring(0, indexOfColon)
    val userName = this.substring(indexOfColon + 1, this.length)
    return UserIdAndName(userId, userName)
}

val dateFormatType = "yyyy/MM/dd HH:mm:ss"

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