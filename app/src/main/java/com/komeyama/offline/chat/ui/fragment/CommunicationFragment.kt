package com.komeyama.offline.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.nearbyclient.ConnectingStatus
import com.komeyama.offline.chat.ui.MainViewModel
import com.komeyama.offline.chat.util.Author
import com.komeyama.offline.chat.util.Message
import com.komeyama.offline.chat.util.toDate
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_communication.*
import javax.inject.Inject
import timber.log.Timber

class CommunicationFragment : Fragment(), TransitionNavigator {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    private var communicationOpponentId = ""
    private var communicationOpponentName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args =
            CommunicationFragmentArgs.fromBundle(
                arguments!!
            )
        communicationOpponentId = args.communicationOpponentId
        communicationOpponentName = args.communicationOpponentName

        (activity?.application as MainApplication).appComponent.injectionToCommunicationFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        viewModel.transitionNavigator = this
        viewModel.insertCommunicatedUser()

        return inflater.inflate(R.layout.fragment_communication, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopRefreshMessages()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.startRefreshMessages()

        val adapter = MessagesListAdapter<Message>(viewModel.currentUserInformation.userId, null)
        messagesList.setAdapter(adapter)

        var oldListSize = 0
        viewModel.selectUserContent(communicationOpponentId).observe(viewLifecycleOwner, Observer { list ->
            list.asReversed().apply{
                this.forEachIndexed { index, value ->
                    if (oldListSize < index + 1) {
                        adapter.addToStart(
                            Message(
                                index.toString(),
                                Author(
                                    value.sendUserId,
                                    value.sendUserName,
                                    ""
                                ),
                                value.sendTime.toDate(),
                                value.content
                            ),true
                        )
                    }
                }
                oldListSize = list.size
            }
        })

        input.setInputListener {
            viewModel.sendMessage(
                it.toString(),
                communicationOpponentId,
                communicationOpponentName
            )
            true
        }

        viewModel.connectingStatus.observe(viewLifecycleOwner, Observer {
            Timber.d("connectingStatus:%s", it)
            if (it == ConnectingStatus.LOST) {
                findNavController().navigate(R.id.action_CommunicationFragment_to_disconnectedMessageDialog)
            }
        })
    }

    override fun showConfirmAcceptanceDialog() {}
    override fun showConfirmFinishCommunication() {
        Timber.d("showConfirmFinishCommunication on CommunicationFragment")
        findNavController().navigate(R.id.action_CommunicationFragment_to_confirmFinishCommunicationDialog)
    }
}