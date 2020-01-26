package com.komeyama.offline.chat.ui

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
import timber.log.Timber
import javax.inject.Inject

class ConfirmAcceptanceDialog : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val args = ConfirmAcceptanceDialogArgs.fromBundle(arguments!!)
        val message: String = getString(R.string.confirm_acceptance_dialog_message) + " " + args.userName
        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.confirm_acceptance_dialog_title)
            .setMessage(message)
            .setNegativeButton(R.string.confirm_acceptance_dialog_ng) { _, _ ->
                Timber.d("dialog fragment ng")
                viewModel.rejectConnection(args.endPointId)
            }
            .setPositiveButton(R.string.confirm_acceptance_dialog_ok) { _, _ ->
                Timber.d("dialog fragment ok")
                viewModel.acceptConnection(args.endPointId)
                findNavController().navigate(
                    ConfirmAcceptanceDialogDirections.actionConfirmAcceptanceDialogToCommunicationFragment(
                        communicationOpponentId = args.id,
                        communicationOpponentName = args.userName
                    )
                )
            }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToConfirmAcceptanceDialog(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}