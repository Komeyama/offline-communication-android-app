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

            Timber.d("current destination")
            navController.currentDestination?.apply {
                Timber.d("current destination: " + this.navigatorName.toString())
            }

            navController.currentDestination?.apply {
                when (this.id) {
                    R.id.CommunicableUserListFragment -> {
                        Timber.d("current destination: 0")
                        navController.navigate(R.id.action_CommunicableUserListFragment_to_ConfirmAcceptanceDialog)
                    }
                    R.id.CommunicationHistoryListFragment -> {
                        Timber.d("current destination: 1")
                        navController.navigate(R.id.action_CommunicationHistoryListFragment_to_ConfirmAcceptanceDialog)
                    }
                    R.id.SettingFragment ->{
                        Timber.d("current destination: 2")
                        navController.navigate(R.id.action_SettingFragment_to_ConfirmAcceptanceDialog)
                    }
                    else -> {
                        Timber.d("current destination: 3")
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
