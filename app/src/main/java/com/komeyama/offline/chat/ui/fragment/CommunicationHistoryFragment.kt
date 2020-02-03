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
import com.komeyama.offline.chat.ui.MainViewModel
import com.komeyama.offline.chat.util.Author
import com.komeyama.offline.chat.util.Message
import com.komeyama.offline.chat.util.toDate
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_communicated.*
import timber.log.Timber
import javax.inject.Inject

class CommunicationHistoryFragment: Fragment(), TransitionNavigator {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    private var communicatedOpponentId = ""
    private var communicatedOpponentName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = CommunicationHistoryFragmentArgs.fromBundle(arguments!!)
        communicatedOpponentId = args.communicatedOpponentId
        communicatedOpponentName = args.communicatedOpponentName

        (activity?.application as MainApplication).appComponent.injectionToCommunicationHistoryFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        viewModel.transitionNavigator = this

        return inflater.inflate(R.layout.fragment_communicated, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = MessagesListAdapter<Message>(viewModel.currentUserInformation.userId, null)
        historyMessagesList.setAdapter(adapter)
        Timber.d("viewModel:currentUserInformation:userId: %s", viewModel.currentUserInformation.userId)

        var oldListSize = 0
        viewModel.selectUserContent(communicatedOpponentId).observe(viewLifecycleOwner, Observer { list ->
            Timber.d("list: %s", list)
            Timber.d("communicationOpponentId: %s", communicatedOpponentId)
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
    }

    override fun showConfirmAcceptanceDialog() {}

    override fun showConfirmFinishCommunication() {
        Timber.d("tap showConfirmFinishCommunication on CommunicationHistoryFragment")
        findNavController().navigateUp()
    }
}