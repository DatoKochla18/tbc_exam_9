package com.example.challenge.presentation.screen.connection

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge.databinding.FragmentConnectionsBinding
import com.example.challenge.presentation.base.BaseFragment
import com.example.challenge.presentation.extension.collectLastFlow
import com.example.challenge.presentation.extension.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionsFragment :
    BaseFragment<FragmentConnectionsBinding>(FragmentConnectionsBinding::inflate) {

    private val viewModel: ConnectionsViewModel by viewModels()
    private val connectionsRecyclerAdapter by lazy {
        ConnectionsRecyclerAdapter()
    }

    override fun bind() {
        binding.apply {
            recyclerConnections.layoutManager = LinearLayoutManager(requireContext())
            recyclerConnections.setHasFixedSize(true)
            recyclerConnections.adapter = connectionsRecyclerAdapter
        }
        viewModel.onEvent(ConnectionEvent.FetchConnections)
    }

    override fun bindViewActionListeners() {
        binding.btnLogOut.setOnClickListener {
            viewModel.onEvent(ConnectionEvent.LogOut)
        }
    }

    override fun bindObserves() {
        collectLastFlow(viewModel.connectionState) {
            viewModel.connectionState.collect {
                Log.d("state", it.toString())
                handleConnectionState(state = it)
            }
        }


        collectLastFlow(viewModel.uiEvent) { event ->
            when (event) {
                ConnectionsViewModel.ConnectionUiEvent.NavigateToLogIn -> handleNavigationEvents()
                is ConnectionsViewModel.ConnectionUiEvent.ShowSnackBar -> toast(event.message)
            }
        }
    }

    private fun handleConnectionState(state: ConnectionState) {
        binding.loaderInclude.loaderContainer.isVisible = state.isLoading
        binding.btnLogOut.isVisible = !state.isLoading

        state.connections?.let {
            connectionsRecyclerAdapter.submitList(it)
        }
    }

    private fun handleNavigationEvents() {
        findNavController().navigate(ConnectionsFragmentDirections.actionConnectionsFragmentToLogInFragment())
    }
}
