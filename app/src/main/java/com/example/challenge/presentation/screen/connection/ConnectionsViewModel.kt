package com.example.challenge.presentation.screen.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge.VSS.presentation.mapper.connection.toPresenter
import com.example.challenge.domain.usecase.connection.GetConnectionsUseCase
import com.example.challenge.domain.usecase.datastore.ClearDataStoreUseCase
import com.example.challenge.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    private val getConnectionsUseCase: GetConnectionsUseCase,
    private val clearDataStoreUseCase: ClearDataStoreUseCase,
) :
    ViewModel() {
    private val _connectionState = MutableStateFlow(ConnectionState())
    val connectionState: SharedFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _uiEvent = Channel<ConnectionSideEffect>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun onEvent(event: ConnectionEvent) {
        when (event) {
            is ConnectionEvent.FetchConnections -> fetchConnections()
            is ConnectionEvent.LogOut -> logOut()
        }
    }

    private fun fetchConnections() {
        _connectionState.update { it.copy(isLoading = true, connections = listOf()) }
        viewModelScope.launch {
            getConnectionsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _connectionState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                connections = resource.data.map { it.toPresenter() })
                        }
                    }

                    is Resource.Error -> _uiEvent.send(ConnectionSideEffect.ShowSnackBar(resource.message))
                }
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            clearDataStoreUseCase()
            _uiEvent.send(ConnectionSideEffect.NavigateToLogIn)
        }
    }


}