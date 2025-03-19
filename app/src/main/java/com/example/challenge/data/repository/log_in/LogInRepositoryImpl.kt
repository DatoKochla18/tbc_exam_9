package com.example.challenge.data.repository.log_in

import com.example.challenge.data.remote.util.HandleResponse
import com.example.challenge.data.mapper.log_in.toDomain
import com.example.challenge.data.mapper.resource.asResource
import com.example.challenge.data.remote.service.log_in.LogInService
import com.example.challenge.domain.manager.UserSessionManager
import com.example.challenge.domain.model.log_in.GetToken
import com.example.challenge.domain.repository.log_in.LogInRepository
import com.example.challenge.domain.user_data_key.PreferenceKeys.TOKEN
import com.example.challenge.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val logInService: LogInService,
    private val userSessionManager: UserSessionManager,
    private val handleResponse: HandleResponse,
) : LogInRepository {
    override suspend fun logIn(email: String, password: String): Flow<Resource<GetToken>> {
        return handleResponse.safeApiCall {
            val data = logInService.logIn(email = email, password = password)
            if (data.isSuccessful) {
                data.body()?.accessToken?.let {
                    userSessionManager.saveValue(TOKEN, it)
                }
            }
            data
        }.asResource {
            it.toDomain()
        }
    }
}