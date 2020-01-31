package com.komeyama.offline.chat.util

import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
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

fun userIdGenerator(): String {
    return ('0'..'z').
        filter { it != ':' }.
        shuffled().subList(0, 10).
        joinToString("")
}

class Message(private val id: String,
              private val author: Author,
              private val date: Date,
              private val text: String) : IMessage {

    override fun getId(): String {
        return id
    }

    override fun getCreatedAt(): Date {
        return date
    }

    override fun getUser(): Author {
        return author
    }

    override fun getText(): String {
        return text
    }
}

class Author(private val id:String,
             private val name:String,
             private val avatar:String) : IUser {

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getAvatar(): String {
        return avatar
    }
}