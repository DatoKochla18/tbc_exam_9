package com.example.challenge.domain.usecase.datastore

import androidx.datastore.preferences.core.Preferences
import com.example.challenge.domain.manager.UserSessionManager
import javax.inject.Inject

class SaveValueToLocalStorageUseCase @Inject constructor(
    private val userSessionManager: UserSessionManager,
) {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) {
        userSessionManager.saveValue(key, value)
    }
}