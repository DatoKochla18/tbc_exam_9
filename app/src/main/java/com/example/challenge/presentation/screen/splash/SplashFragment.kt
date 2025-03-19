package com.example.challenge.presentation.screen.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.challenge.databinding.FragmentSplashBinding
import com.example.challenge.presentation.base.BaseFragment
import com.example.challenge.presentation.extension.collectLastFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModels()

    override fun bind() {

    }

    override fun bindViewActionListeners() {

    }

    override fun bindObserves() {
        collectLastFlow(viewModel.uiEvent) { event ->
            when (event) {
                SplashViewModel.SplashUiEvent.NavigateToConnections -> findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToConnectionsFragment()
                )

                SplashViewModel.SplashUiEvent.NavigateToLogIn -> findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToLogInFragment()
                )

            }
        }
    }
}

