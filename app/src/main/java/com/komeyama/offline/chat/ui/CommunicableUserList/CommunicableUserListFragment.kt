package com.komeyama.offline.chat.ui.CommunicableUserList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.di.CommunicableUserListViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import javax.inject.Inject

class CommunicableUserListFragment :Fragment(){

    @Inject
    lateinit var viewModelFactory: CommunicableUserListViewModelFactory
    private lateinit var viewModel: CommunicableUserListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_communicable, container, false)

        (activity?.application as MainApplication).appComponent.injectionToCommunicableUserListFragment(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommunicableUserListViewModel::class.java)
        viewModel.test()

        return view
    }
}