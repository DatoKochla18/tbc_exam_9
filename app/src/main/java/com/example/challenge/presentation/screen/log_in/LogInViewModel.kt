package com.example.challenge.presentation.screen.log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge.domain.usecase.log_in.LogInUseCase
import com.example.challenge.domain.usecase.validator.EmailValidatorUseCase
import com.example.challenge.domain.usecase.validator.PasswordValidatorUseCase
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
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val emailValidator: EmailValidatorUseCase,
    private val passwordValidator: PasswordValidatorUseCase,
) : ViewModel() {
    private val _logInState = MutableStateFlow(LogInState())
    val logInState: SharedFlow<LogInState> = _logInState.asStateFlow()

    private val _uiEvent = Channel<LogInSideEffect>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun onEvent(event: LogInEvent) {
        when (event) {
            is LogInEvent.LogIn -> validateForm(email = event.email, password = event.password)
            LogInEvent.ResetErrorMessage -> _logInState.update { it.copy(error = null) }
        }
    }

    private fun logIn(email: String, password: String) {
        _logInState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            logInUseCase(email = email, password = password).collect {
                when (it) {
                    is Resource.Success -> {
                        _uiEvent.send(LogInSideEffect.NavigateToConnections)
                    }

                    is Resource.Error -> _uiEvent.send(LogInSideEffect.ShowSnackBar(it.message))
                }
            }
        }
    }

    private fun validateForm(email: String, password: String) {
        val isEmailValid = emailValidator(email)
        val isPasswordValid = passwordValidator(password)

        val areFieldsValid =
            listOf(isEmailValid, isPasswordValid)
                .all { it }

        if (!areFieldsValid) {
            _logInState.update { it.copy(error = "Validation Error") }
            return
        }

        logIn(email = email, password = password)
    }



}



