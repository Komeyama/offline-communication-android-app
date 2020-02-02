package com.komeyama.offline.chat.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class ConfirmFinishCommunicationDialog: DialogFragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.confirm_request_dialog_title)
            .setMessage(R.string.confirm_finish_communication)
            .setNegativeButton(R.string.confirm_request_dialog_ng) { _, _ ->
                Timber.d("dialog finish communication fragment ng")
            }
            .setPositiveButton(R.string.confirm_request_dialog_ok) { _, _ ->
                Timber.d("dialog finish communication fragment ok")
                viewModel.finishCommunication()
                findNavController().navigate(R.id.action_confirmFinishCommunicationDialog_to_CommunicableUserListFragment)
            }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToConfirmFinishCommunicationDialog(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }


}