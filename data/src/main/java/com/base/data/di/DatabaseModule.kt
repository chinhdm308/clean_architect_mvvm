package com.base.data.di

import android.app.Application
import com.base.data.local.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): LocalDatabase = LocalDatabase.getInstance(app)

    @Provides
    @Singleton
    fun providePostDao(database: LocalDatabase) = database.postDao()
}