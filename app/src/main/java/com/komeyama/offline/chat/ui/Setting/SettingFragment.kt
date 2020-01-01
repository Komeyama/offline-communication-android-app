package com.komeyama.offline.chat.ui.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.SettingViewModelFactory
import com.komeyama.offline.chat.ui.CommunicationHistoryList.CommunicationHistoryListViewModel
import javax.inject.Inject

class SettingFragment :Fragment(){

    @Inject
    lateinit var viewModelFactory: SettingViewModelFactory
    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_communicable, container, false)

        (activity?.application as MainApplication).appComponent.injectionToSettingFragment(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingViewModel::class.java)
        viewModel.test()

        return view
    }
}