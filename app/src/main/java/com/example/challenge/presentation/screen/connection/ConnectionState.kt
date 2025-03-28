package com.example.challenge.presentation.screen.connection

import com.example.challenge.presentation.model.connection.Connection

data class ConnectionState(
    val isLoading: Boolean = false,
    val connections: List<Connection>? = null,
    val errorMessage: String? = null,
)
