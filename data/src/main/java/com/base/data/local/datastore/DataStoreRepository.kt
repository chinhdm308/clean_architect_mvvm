package com.base.data.local.datastore

interface DataStoreRepository {
    suspend fun saveToken(token: String)

    suspend fun getToken(): String

    suspend fun clear()
}