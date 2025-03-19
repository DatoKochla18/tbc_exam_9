package com.example.challenge.domain.usecase.datastore

import com.example.challenge.domain.manager.UserSessionManager
import javax.inject.Inject

class ClearDataStoreUseCase @Inject constructor(private val userSessionManager: UserSessionManager) {
    suspend operator fun invoke() {
        userSessionManager.clear()
    }
}