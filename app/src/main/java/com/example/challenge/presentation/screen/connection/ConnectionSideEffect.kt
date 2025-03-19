package com.example.challenge.presentation.screen.connection

sealed interface ConnectionSideEffect {
    object NavigateToLogIn : ConnectionSideEffect
    data class ShowSnackBar(val message: String) : ConnectionSideEffect
}