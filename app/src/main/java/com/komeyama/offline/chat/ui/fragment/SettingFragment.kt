package com.komeyama.offline.chat.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.databinding.FragmentCommunicableUserListBinding
import com.komeyama.offline.chat.databinding.FragmentSettingBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class SettingFragment :Fragment(), TransitionNavigator{

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToSettingFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        val binding: FragmentSettingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.transitionNavigator = this
        binding.lifecycleOwner = this
        binding.focusGetDummyView.requestFocus()
        viewModel.getUserName()
        viewModel.toolbarTitleText.postValue(getString(R.string.toolbar_title_setting))

        binding.root.setOnClickListener {
            closeKeyBoardAndGetFocus(binding, it)
        }

        binding.settingSaveButton.setOnClickListener {
            closeKeyBoardAndGetFocus(binding, it)
            viewModel.updateUserName()
            Toast.makeText(context , R.string.setting_saved_toast_massage, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun closeKeyBoardAndGetFocus(binding: FragmentSettingBinding, view: View) {
        binding.focusGetDummyView.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.apply {
            this.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun showConfirmAcceptanceDialog() {
        findNavController().navigate(
            SettingFragmentDirections.
                actionSettingFragmentToConfirmAcceptanceDialog(
                    id = viewModel.communicationOpponentInfo.id,
                    userName = viewModel.communicationOpponentInfo.name,
                    endPointId = viewModel.communicationOpponentInfo.endpointId
                )
        )
    }

    override fun tapBackButtonOfToolbar() {}

    override fun tapFirstItemOfMenuList() {
        findNavController().navigate(R.id.action_SettingFragment_to_licenseFragment)
    }
}