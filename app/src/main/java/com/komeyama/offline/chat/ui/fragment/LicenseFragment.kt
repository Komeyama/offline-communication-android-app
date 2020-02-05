package com.komeyama.offline.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_license.*
import javax.inject.Inject

class LicenseFragment: Fragment(), TransitionNavigator{

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        (activity?.application as MainApplication).appComponent.injectionToLicenseFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        viewModel.transitionNavigator = this
        viewModel.toolbarTitleText.postValue(getString(R.string.toolbar_title_licenses))

        return inflater.inflate(R.layout.fragment_license, container, false)
    }

    override fun onResume() {
        super.onResume()
        license_view.loadUrl("file:///android_asset/licenses.html")
    }

    override fun showConfirmAcceptanceDialog() {
        findNavController().navigate(
            LicenseFragmentDirections.
                actionLicenseFragmentToConfirmAcceptanceDialog(
                    id = viewModel.communicationOpponentInfo.id,
                    userName = viewModel.communicationOpponentInfo.name,
                    endPointId = viewModel.communicationOpponentInfo.endpointId
                )
            )
    }

    override fun tapBackButtonOfToolbar() {
        findNavController().navigateUp()
    }

    override fun tapFirstItemOfMenuList() {}


}