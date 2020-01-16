package com.komeyama.offline.chat.ui.communication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.komeyama.offline.chat.R
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_communication.*
import java.util.*

class CommunicationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_communication, container, false)
    }

    override fun onResume() {
        super.onResume()
        val adapter = MessagesListAdapter<Message>("a0", null)
        messagesList.setAdapter(adapter)

        val messages: List<Message> = listOf(
            Message("0", Author("a0","hoge","avator"),Date(),"hello"),
            Message("1", Author("a1","hoge","avator"),Date(),"hello!")
        )

        messages.forEach{
            adapter.addToStart(it,true)
        }
    }
}

class Message(private val id: String,
              private val author: Author,
              private val date:Date,
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