package com.komeyama.offline.chat.util

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