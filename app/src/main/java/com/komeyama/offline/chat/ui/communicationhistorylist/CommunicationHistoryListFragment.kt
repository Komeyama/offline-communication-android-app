package com.komeyama.offline.chat.ui.communicationhistorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.CommunicationHistoryListViewModelFactory
import javax.inject.Inject

class CommunicationHistoryListFragment :Fragment(){

    @Inject
    lateinit var viewModelFactory: CommunicationHistoryListViewModelFactory
    private lateinit var viewModel: CommunicationHistoryListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        (activity?.application as MainApplication).appComponent.injectionToCommunicationHistoryFragment(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommunicationHistoryListViewModel::class.java)
        viewModel.test()

        return view
    }
}