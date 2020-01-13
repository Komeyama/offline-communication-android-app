package com.komeyama.offline.chat.ui.communicableuserlist

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import timber.log.Timber

class ConfirmAcceptanceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireActivity())
            .setTitle("Dialog Title")
            .setMessage("Dialog Message")
            .setPositiveButton("OK") { _, _ ->
                Timber.d("dialog fragment ok")
            }
            .create()
    }
}