package com.komeyama.offline.chat.ui.communication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_communication.*
import java.util.*
import javax.inject.Inject

class CommunicationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    private var communicationOpponentId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = CommunicationFragmentArgs.fromBundle(arguments!!)
        communicationOpponentId = args.communicationOpponentId
        (activity?.application as MainApplication).appComponent.injectionToCommunicationFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        return inflater.inflate(R.layout.fragment_communication, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.startRefreshMessages()

        /**
         * todo: change 'senderId'(myself)
         */
        val adapter = MessagesListAdapter<Message>("myself", null)
        messagesList.setAdapter(adapter)

        var oldListSize = 0

        viewModel.selectedUserContent(communicationOpponentId).observe(viewLifecycleOwner, Observer { list ->
            list.asReversed().apply{
                this.forEachIndexed { index, value ->
                    if (oldListSize < index + 1) {
                        adapter.addToStart(
                            Message(
                                index.toString(),
                                Author(value.sendUserId, value.sendUserName,""),
                                value.sendTime,
                                value.content),true
                        )
                    }
                }
                oldListSize = list.size
            }
        })

    }
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