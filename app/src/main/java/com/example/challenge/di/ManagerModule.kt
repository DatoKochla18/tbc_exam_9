package com.example.challenge.di


import com.example.challenge.data.manager.UserSessionManagerImpl
import com.example.challenge.domain.manager.UserSessionManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindUserSessionManager(impl: UserSessionManagerImpl): UserSessionManager
}