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
import com.komeyama.offline.chat.MainApplication
import com.komeyama.offline.chat.R
import com.komeyama.offline.chat.databinding.FragmentCommunicationHistoryListBinding
import com.komeyama.offline.chat.databinding.HistoryItemBinding
import com.komeyama.offline.chat.di.MainViewModelFactory
import com.komeyama.offline.chat.domain.HistoryUser
import com.komeyama.offline.chat.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

class CommunicationHistoryListFragment: Fragment(), TransitionNavigator{

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelAdapter: CommunicationHistoryListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as MainApplication).appComponent.injectionToCommunicationHistoryFragment(this)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        val binding: FragmentCommunicationHistoryListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_communication_history_list,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.transitionNavigator = this

        navController = findNavController()
        viewModelAdapter = CommunicationHistoryListAdapter(HistoryUserClick{
            Timber.d("click history item: %s",it)
            navController.navigate(
                CommunicationHistoryListFragmentDirections.actionCommunicationHistoryListFragmentToCommunicationHistoryFragment(
                    communicatedOpponentId = it.id,
                    communicatedOpponentName = it.name
                )
            )
        })

        binding.recyclerHistoryView.findViewById<RecyclerView>(R.id.recycler_history_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.checkCommunicatedUserName()
        viewModel.communicatedList.observe(viewLifecycleOwner, Observer { lists ->
            lists?.apply {
                viewModelAdapter.historyUsers = lists
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopCheckCommunicatedUserName()
    }

    override fun showConfirmAcceptanceDialog() {
        navController.navigate(
            CommunicationHistoryListFragmentDirections.
                actionCommunicationHistoryListFragmentToConfirmAcceptanceDialog(
                    id = viewModel.communicationOpponentInfo.id,
                    userName = viewModel.communicationOpponentInfo.name,
                    endPointId = viewModel.communicationOpponentInfo.endpointId
                )
        )
    }

    override fun showConfirmFinishCommunication() {}
}

class HistoryUserClick(val user:(HistoryUser) -> Unit) {
    fun onClick(user: HistoryUser) {
        user(user)
    }
}

class CommunicationHistoryListHolder(val viewDataBinding: HistoryItemBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.history_item
    }
}

class CommunicationHistoryListAdapter(val callback: HistoryUserClick) : RecyclerView.Adapter<CommunicationHistoryListHolder>() {
    var historyUsers: List<HistoryUser> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunicationHistoryListHolder {
        val withDataBinding: HistoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CommunicationHistoryListHolder.LAYOUT,
            parent,
            false
        )
        return CommunicationHistoryListHolder(withDataBinding)
    }

    override fun getItemCount() = historyUsers.size

    override fun onBindViewHolder(holder: CommunicationHistoryListHolder, position: Int) {
        holder.viewDataBinding.also {
            it.userList = historyUsers[position]
            it.userListCallback = callback
        }
    }

}