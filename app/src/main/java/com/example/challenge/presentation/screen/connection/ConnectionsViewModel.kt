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

    private val _uiEvent = Channel<ConnectionUiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun onEvent(event: ConnectionEvent) {
        when (event) {
            is ConnectionEvent.FetchConnections -> fetchConnections()
            is ConnectionEvent.LogOut -> logOut()
        }
    }

    private fun fetchConnections() {
        viewModelScope.launch {
            getConnectionsUseCase().collect {
                when (it) {
                    is Resource.Loading -> _connectionState.update { currentState ->
                        currentState.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _connectionState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                connections = it.data.map { it.toPresenter() })
                        }
                    }

                    is Resource.Error -> _uiEvent.send(ConnectionUiEvent.ShowSnackBar(it.message))
                }
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            clearDataStoreUseCase()
            _uiEvent.send(ConnectionUiEvent.NavigateToLogIn)
        }
    }

    sealed interface ConnectionUiEvent {
        object NavigateToLogIn : ConnectionUiEvent
        data class ShowSnackBar(val message: String) : ConnectionUiEvent
    }
}