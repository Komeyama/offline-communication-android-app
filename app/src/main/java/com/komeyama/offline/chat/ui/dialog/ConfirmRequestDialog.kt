package com.komeyama.offline.chat.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class ConfirmRequestDialog : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val args =
            ConfirmAcceptanceDialogArgs.fromBundle(arguments!!)
        val message: String = getString(R.string.confirm_request_dialog_message) + " " + args.userName + " ?"
        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.confirm_request_dialog_title)
            .setMessage(message)
            .setNegativeButton(R.string.confirm_request_dialog_ng) { _, _ ->
                Timber.d("dialog request fragment ng")
                viewModel.rejectConnection(args.endPointId)
            }
            .setPositiveButton(R.string.confirm_request_dialog_ok) { _, _ ->
                Timber.d("dialog request fragment ok")
                viewModel.requestConnection(args.endPointId)
            }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToConfirmRequestDialog(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}