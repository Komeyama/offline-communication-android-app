package com.komeyama.offline.chat.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.komeyama.offline.chat.R
import timber.log.Timber

class DisconnectedMessageDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = AlertDialog.Builder(requireActivity())
            .setMessage(R.string.disconnected_message_dialog_message)
            .setPositiveButton(R.string.confirm_request_dialog_ok) { _, _ ->
                Timber.d("dialog disconnected message fragment ok")
                findNavController().navigate(R.id.action_disconnectedMessageDialog_to_CommunicableUserListFragment)
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}