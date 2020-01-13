package com.komeyama.offline.chat.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.di.MainViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class ConfirmAcceptanceDialog : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val args =
            ConfirmAcceptanceDialogArgs.fromBundle(
                arguments!!
            )
        return AlertDialog.Builder(requireActivity())
            .setTitle("Calling!")
            .setMessage("Calling from " + args.userName)
            .setNegativeButton("Reject") { _, _ ->
                Timber.d("dialog fragment ng")
                viewModel.rejectConnection(args.endPointId)
            }
            .setPositiveButton("Accept") { _, _ ->
                Timber.d("dialog fragment ok")
                viewModel.acceptConnection(args.endPointId)
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