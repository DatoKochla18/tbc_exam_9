package com.example.challenge.presentation.screen.connection

sealed class ConnectionEvent {
    object FetchConnections : ConnectionEvent()
    object LogOut : ConnectionEvent()
}