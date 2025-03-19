package com.example.challenge.domain.repository.connection

import com.example.challenge.domain.util.Resource
import com.example.challenge.domain.model.connection.GetConnection
import kotlinx.coroutines.flow.Flow

interface ConnectionsRepository {
    suspend fun getConnections(): Flow<Resource<List<GetConnection>>>

}