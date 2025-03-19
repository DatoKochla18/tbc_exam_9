package com.example.challenge.domain.usecase.datastore

import androidx.datastore.preferences.core.Preferences
import com.example.challenge.domain.manager.UserSessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetValueFromLocalStorageUseCase @Inject constructor(
    private val userSessionManager: UserSessionManager
) {
    operator fun <T> invoke(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return userSessionManager.readValue(key, defaultValue)
    }
}