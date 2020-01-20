package com.komeyama.offline.chat.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.databinding.FragmentInitialBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import javax.inject.Inject

class InitialSettingDialog : DialogFragment(){

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentInitialBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_initial, null, false)
        val view = binding.root
        builder.setView(view)

        return builder.create()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToInitialSettingDialog(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}