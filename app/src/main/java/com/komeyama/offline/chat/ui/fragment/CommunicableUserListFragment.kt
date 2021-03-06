package com.komeyama.offline.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.databinding.ActiveUserItemBinding
import com.komeyama.offline.chat.databinding.FragmentCommunicableUserListBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.nearbyclient.RequestResult
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class CommunicableUserListFragment :Fragment(), TransitionNavigator{

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelAdapter: CommunicableUserListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.application as MainApplication).appComponent.injectionToCommunicableUserListFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        val binding: FragmentCommunicableUserListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_communicable_user_list,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.transitionNavigator = this

        navController = findNavController()
        viewModelAdapter =
            CommunicableUserListAdapter(ActiveUserClick {
                viewModel.communicationOpponentInfo =
                    CommunicationOpponentInfo(
                        it.id,
                        it.name,
                        it.endPointId
                    )
                navController.navigate(
                    CommunicableUserListFragmentDirections.actionCommunicableUserListFragmentToConfirmRequestDialog(
                        id = it.id,
                        userName = it.name,
                        endPointId = it.endPointId
                    )
                )
            })

        binding.recyclerView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        viewModel.toolbarTitleText.postValue(getString(R.string.toolbar_title_active_user_list))

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.activeUserList.observe(viewLifecycleOwner, Observer<List<ActiveUser>> { lists ->
            lists?.apply {
                viewModelAdapter.activeUsers = lists
            }
        })
        viewModel.requestResult.observe(viewLifecycleOwner, Observer<RequestResult> {
            Timber.d("request result: %s", it.toString())
            when(it) {
                RequestResult.SUCCESS -> {
                    navController.navigate(
                        CommunicableUserListFragmentDirections.actionCommunicableUserListFragmentToCommunicationFragment(
                            communicationOpponentId = viewModel.communicationOpponentInfo.id,
                            communicationOpponentName = viewModel.communicationOpponentInfo.name
                        )
                    )
                }
                else -> {
                    Timber.d("request result else: %s", it.toString())
                }
            }
        })
    }

    override fun showConfirmAcceptanceDialog() {
        navController.navigate(
            CommunicableUserListFragmentDirections.
                actionCommunicableUserListFragmentToConfirmAcceptanceDialog(
                    id = viewModel.communicationOpponentInfo.id,
                    userName = viewModel.communicationOpponentInfo.name,
                    endPointId = viewModel.communicationOpponentInfo.endpointId
                )
        )
    }

    override fun tapBackButtonOfToolbar() {}
    override fun tapFirstItemOfMenuList() {}

}

class ActiveUserClick(val user:(ActiveUser) -> Unit) {
    fun onClick(user: ActiveUser) {
        user(user)
    }
}

class CommunicableUserListHolder(val viewDataBinding: ActiveUserItemBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.active_user_item
    }
}

class CommunicableUserListAdapter(val callback: ActiveUserClick) : RecyclerView.Adapter<CommunicableUserListHolder>(){
    var activeUsers: List<ActiveUser> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunicableUserListHolder {
        val withDataBinding: ActiveUserItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CommunicableUserListHolder.LAYOUT,
            parent,
            false)
        return CommunicableUserListHolder(withDataBinding)
    }

    override fun getItemCount() = activeUsers.size

    override fun onBindViewHolder(holder: CommunicableUserListHolder, position: Int) {
        holder.viewDataBinding.also {
            it.userList = activeUsers[position]
            it.userListCallback = callback
        }
    }

}

data class CommunicationOpponentInfo(
    val id: String,
    val name: String,
    val endpointId: String
)
