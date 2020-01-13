package com.komeyama.offline.chat.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.ui.communicableuserlist.CommunicableUserListFragmentDirections
import com.komeyama.offline.chat.ui.communicationhistorylist.CommunicationHistoryListFragmentDirections
import com.komeyama.offline.chat.ui.setting.SettingFragmentDirections
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottom_navigation, navController)
        navController.addOnDestinationChangedListener{ _ , destination , _ ->
            when (destination.id) {
                R.id.CommunicationFragment -> setVisibilityOfBottomNavigation(false)
                else -> setVisibilityOfBottomNavigation(true)
            }
        }

        (application as MainApplication).appComponent.injectionToMainActivity(this)
        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        startNearbyClientWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun startNearbyClient() {
        viewModel.startNearbyClient()
        viewModel.requestedUser.observe(this, Observer<ActiveUser> { user ->
            navController.currentDestination?.apply {
                when (this.id) {
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
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

}
