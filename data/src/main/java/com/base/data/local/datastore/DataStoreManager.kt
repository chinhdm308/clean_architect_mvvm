package com.base.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext val context: Context
) : DataStoreRepository {

    /**
     * Single data store instance
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

    private val dataStore: DataStore<Preferences> by lazy {
        context.dataStore
    }

    companion object {
        private const val DATA_STORE_NAME = "dataStore_AppName"

        val keyToken = stringPreferencesKey("keyToken")
    }

    override suspend fun saveToken(token: String) = dataStore.put(keyToken, token)

    override suspend fun getToken(): String = dataStore.get(keyToken)

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}

private suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { settings ->
        settings.putAll()
        settings[key] = value
    }
}

private suspend inline fun <reified T> DataStore<Preferences>.get(key: Preferences.Key<T>): T {
    return data.map { preferences ->
        preferences[key] ?: defaultValue()
    }.first()
}

/**
 * using Gson to parser Object to String
 */
private inline fun <reified T> defaultValue(): T = when (T::class) {
    Boolean::class -> false as T
    Int::class -> 0 as T
    Long::class -> 0L as T
    Float::class -> 0f as T
    String::class -> "" as T
    Set::class -> mutableSetOf<String>() as T
    else -> throw IllegalStateException("Type value not support")
}