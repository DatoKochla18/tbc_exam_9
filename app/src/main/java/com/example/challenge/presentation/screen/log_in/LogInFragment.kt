package com.example.challenge.presentation.screen.log_in

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge.databinding.FragmentLogInBinding
import com.example.challenge.presentation.base.BaseFragment
import com.example.challenge.presentation.event.log_in.LogInEvent
import com.example.challenge.presentation.extension.collectLastFlow
import com.example.challenge.presentation.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()

    override fun bind() {

    }

    override fun bindViewActionListeners() {
        binding.btnLogIn.setOnClickListener {
            logIn()
        }
    }

    override fun bindObserves() {
        collectLastFlow(viewModel.logInState) {
            handleLogInState(logInState = it)
        }


        collectLastFlow(viewModel.uiEvent) { event ->
            when (event) {
                LogInViewModel.LogInUiEvent.NavigateToConnections -> handleNavigationEvents()
                is LogInViewModel.LogInUiEvent.ShowSnackBar -> binding.root.showSnackBar(event.message)
            }
        }
    }


    private fun logIn() {
        viewModel.onEvent(
            LogInEvent.LogIn(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        )
    }

    private fun handleLogInState(logInState: LogInState) {
        binding.loaderInclude.loaderContainer.visibility =
            if (logInState.isLoading) View.VISIBLE else View.GONE

        logInState.error?.let {
            binding.root.showSnackBar(message = it)
            viewModel.onEvent(LogInEvent.ResetErrorMessage)
        }
    }

    private fun handleNavigationEvents() {
        findNavController().navigate(
            LogInFragmentDirections.actionLogInFragmentToConnectionsFragment()
        )
    }
}

