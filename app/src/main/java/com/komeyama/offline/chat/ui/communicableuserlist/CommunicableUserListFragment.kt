package com.komeyama.offline.chat.ui.communicableuserlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.databinding.ActiveUserItemBinding
import com.komeyama.offline.chat.databinding.FragmentCommunicableUserListBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class CommunicableUserListFragment :Fragment(){

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelAdapter: CommunicableUserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity?.application as MainApplication).appComponent.injectionToCommunicableUserListFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        Timber.d("viewmodel: " + viewModel)

        val binding: FragmentCommunicableUserListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_communicable_user_list,
            container,
            false)

        binding.viewModel = viewModel

        viewModelAdapter = CommunicableUserListAdapter(ActiveUserClick {
            Timber.d("active user click: " + it.id + ", " +it.name)
            findNavController().navigate(R.id.action_CommunicableUserListFragment_to_CommunicationFragment)
        })

        binding.recyclerView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }
}

class ActiveUserClick(val user:(ActiveUser) -> Unit) {
    fun onClick(user:ActiveUser) {
        user(user)
    }
}

class CommunicableUserListHolder(val viewDataBinding: ActiveUserItemBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.active_user_item
    }
}

class CommunicableUserListAdapter(val callback:ActiveUserClick) : RecyclerView.Adapter<CommunicableUserListHolder>(){
    var activeUsers: MutableList<ActiveUser> = mutableListOf(
        ActiveUser("0","dummy0"),
        ActiveUser("1","dummy1"),
        ActiveUser("2","dummy2"),
        ActiveUser("3","dummy3"),
        ActiveUser("4","dummy4"),
        ActiveUser("5","dummy5"),
        ActiveUser("6","dummy6"),
        ActiveUser("7","dummy7"),
        ActiveUser("8","dummy8")
    )

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
