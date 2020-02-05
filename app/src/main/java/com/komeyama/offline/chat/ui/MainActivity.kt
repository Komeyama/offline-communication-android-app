package com.komeyama.offline.chat.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
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
import com.komeyama.offline.chat.ui.fragment.CommunicationOpponentInfo
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
    private var menuItem: MenuItem? = null

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

            setVisibilityOfBottomNavigation(true)
            app_back_button.visibility = View.GONE
            menuItem?.isVisible = false

            when (currentFragment) {
                R.id.CommunicationFragment, R.id.communicationHistoryFragment -> {
                    setVisibilityOfBottomNavigation(false)
                    app_back_button.visibility = View.VISIBLE
                }
                R.id.SettingFragment -> {
                    menuItem?.isVisible = true
                }
                R.id.licenseFragment -> {
                    app_back_button.visibility = View.VISIBLE
                }
            }
        }

        // Set ViewModel
        (application as MainApplication).appComponent.injectionToMainActivity(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        //Set Toolbar
        val toolbar = toolbar as Toolbar
        toolbar.title = getString(R.string.toolbar_title)

        // Set Back Button Action
        setSupportActionBar(toolbar)

        app_back_button.setOnClickListener {
            Timber.d("tap app_back_button")
            viewModel.transitionNavigator.tapBackButtonOfToolbar()
        }

        startNearbyClientWithPermissionCheck()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_navigation, menu)
        menuItem = menu?.getItem(0)
        menuItem?.setOnMenuItemClickListener {
            Timber.d("menu: %s", it)
            viewModel.transitionNavigator.tapFirstItemOfMenuList()
            true
        }
        menuItem?.isVisible = false
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.d("tap onKeyDown")
        viewModel.transitionNavigator.tapBackButtonOfToolbar()
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
                viewModel.communicationOpponentInfo =
                    CommunicationOpponentInfo(
                        user.id,
                        user.name,
                        user.endPointId
                    )
                viewModel.transitionNavigator.showConfirmAcceptanceDialog()
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
}