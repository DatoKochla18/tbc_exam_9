package com.example.challenge.di

import com.example.challenge.data.repository.connection.ConnectionsRepositoryImpl
import com.example.challenge.data.repository.log_in.LogInRepositoryImpl
import com.example.challenge.domain.repository.connection.ConnectionsRepository
import com.example.challenge.domain.repository.log_in.LogInRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun provideLoginRepository(
        impl: LogInRepositoryImpl,
    ): LogInRepository

    @Singleton
    @Binds
    abstract fun provideConnectionsRepository(
        impl: ConnectionsRepositoryImpl,
    ): ConnectionsRepository
}