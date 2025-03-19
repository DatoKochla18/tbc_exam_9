package com.example.challenge.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge.domain.usecase.datastore.GetValueFromLocalStorageUseCase
import com.example.challenge.domain.user_data_key.PreferenceKeys.TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getValueFromLocalStorageUseCase: GetValueFromLocalStorageUseCase) :
    ViewModel() {

    private val _uiEvent = MutableSharedFlow<SplashUiEvent>()
    val uiEvent: SharedFlow<SplashUiEvent> get() = _uiEvent

    init {
        readSession()
    }

    private fun readSession() {
        viewModelScope.launch {
            getValueFromLocalStorageUseCase(TOKEN, "").collect {
                if (it.isEmpty())
                    _uiEvent.emit(SplashUiEvent.NavigateToLogIn)
                else
                    _uiEvent.emit(SplashUiEvent.NavigateToConnections)
            }
        }
    }

    sealed interface SplashUiEvent {
        object NavigateToLogIn : SplashUiEvent
        object NavigateToConnections : SplashUiEvent

    }
}