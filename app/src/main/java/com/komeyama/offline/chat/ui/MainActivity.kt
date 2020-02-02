package com.komeyama.offline.chat.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.databinding.ActivityMainBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.nearbyclient.ConnectionType
import com.komeyama.offline.chat.ui.fragment.CommunicableUserListFragmentDirections
import com.komeyama.offline.chat.ui.fragment.CommunicationHistoryListFragmentDirections
import com.komeyama.offline.chat.ui.fragment.SettingFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private var currentFragment = R.id.CommunicableUserListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set DataBinding
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // Set BottomNavigation
        navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottom_navigation, navController)
        navController.addOnDestinationChangedListener{ _ , destination , _ ->
            currentFragment = destination.id
            when (currentFragment) {
                R.id.CommunicationFragment, R.id.communicationHistoryFragment -> setVisibilityOfBottomNavigation(false)
                else -> setVisibilityOfBottomNavigation(true)
            }
        }

        // Set ViewModel
        (application as MainApplication).appComponent.injectionToMainActivity(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        startNearbyClientWithPermissionCheck()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.d("onSupportNavigateUp")
        viewModel.reStartNearbyClient()
        return super.onKeyDown(keyCode, event)
    }

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun startNearbyClient() {
        viewModel.isExistUserInformation.observe(this, Observer<Boolean>{
            if (!it) {
                navController.navigate(CommunicableUserListFragmentDirections.actionCommunicableUserListFragmentToInitialSettingDialog())
            }
        })
        viewModel.invitedInfo.observe(this, Observer<Iterable<*>> {
            Timber.d("invitedInfo: %s",it)
            val user:ActiveUser = it.filterIsInstance<ActiveUser>()[0]
            val type = it.filterIsInstance<ConnectionType>()[0]
            if (type == ConnectionType.RECEIVER && user.id != "" && user.name != "" && user.endPointId != "") {
                currentFragmentToConfirmAcceptanceDialog(user)
            }
        })
        viewModel.checkCommunicatedUserName()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun setVisibilityOfBottomNavigation(isVisible:Boolean) {
        if (isVisible) {
            bottom_navigation.visibility = View.VISIBLE
        } else {
            bottom_navigation.visibility = View.GONE
        }
    }

    private fun currentFragmentToConfirmAcceptanceDialog(user: ActiveUser) {
        when(currentFragment) {
            R.id.CommunicableUserListFragment -> {
                navController.navigate(
                    CommunicableUserListFragmentDirections.
                        actionCommunicableUserListFragmentToConfirmAcceptanceDialog(
                            id = user.id,
                            userName = user.name,
                            endPointId = user.endPointId
                        )
                )
            }
            R.id.CommunicationHistoryListFragment -> {
                navController.navigate(
                    CommunicationHistoryListFragmentDirections.
                        actionCommunicationHistoryListFragmentToConfirmAcceptanceDialog(
                            id = user.id,
                            userName = user.name,
                            endPointId = user.endPointId
                        )
                )
            }
            R.id.SettingFragment ->{
                navController.navigate(
                    SettingFragmentDirections.
                        actionSettingFragmentToConfirmAcceptanceDialog(
                            id = user.id,
                            userName = user.name,
                            endPointId = user.endPointId
                        )
                )
            }
            else -> {
                viewModel.rejectConnection(user.endPointId)
            }
        }
    }

}
