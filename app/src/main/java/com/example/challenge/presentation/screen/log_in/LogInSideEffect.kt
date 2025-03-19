package com.example.challenge.presentation.screen.log_in

sealed interface LogInSideEffect {
    object NavigateToConnections : LogInSideEffect
    data class ShowSnackBar(val message: String) : LogInSideEffect
}