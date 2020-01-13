package com.komeyama.offline.chat.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class SettingFragment :Fragment(){

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        (activity?.application as MainApplication).appComponent.injectionToSettingFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        return view
    }
}