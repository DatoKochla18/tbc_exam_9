package com.example.challenge.presentation.screen.log_in

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge.databinding.FragmentLogInBinding
import com.example.challenge.presentation.base.BaseFragment
import com.example.challenge.presentation.extension.collectLastFlow
import com.example.challenge.presentation.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()


    private fun bindViewActionListeners() {
        binding.btnLogIn.setOnClickListener {
            logIn()
        }
    }

    override fun bindObserves() {
        bindViewActionListeners()
        collectLastFlow(viewModel.logInState) {
            handleLogInState(logInState = it)
        }


        collectLastFlow(viewModel.uiEvent) { event ->
            when (event) {
                LogInSideEffect.NavigateToConnections -> handleNavigationEvents()
                is LogInSideEffect.ShowSnackBar -> binding.root.showSnackBar(event.message)
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
        binding.loaderInclude.loaderContainer.isVisible = logInState.isLoading

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

