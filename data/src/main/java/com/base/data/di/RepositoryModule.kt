package com.base.data.di

import com.base.data.repositories.JsonPlaceHolderRemoteRepoImpl
import com.base.domain.repositories.JsonPlaceHolderRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindJsonPlaceHolderRemoteRepository(jsonPlaceHolderRepoImpl: JsonPlaceHolderRemoteRepoImpl): JsonPlaceHolderRemoteRepository
}