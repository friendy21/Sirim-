package com.sirim.qrscanner.core.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.remove
import com.sirim.qrscanner.core.domain.model.UserCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenStorage(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("auth_token")
    private val usernameKey = stringPreferencesKey("username")
    private val passwordKey = stringPreferencesKey("password")
    private val lastSyncKey = longPreferencesKey("last_sync")

    val token: Flow<String?> = dataStore.data.map { it[tokenKey] }

    fun credentials(): Flow<UserCredentials?> = dataStore.data.map { prefs ->
        val username = prefs[usernameKey]
        val password = prefs[passwordKey]
        if (username != null && password != null) UserCredentials(username, password) else null
    }

    fun lastSync(): Flow<Long?> = dataStore.data.map { prefs -> prefs[lastSyncKey] }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[tokenKey] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }

    suspend fun updateCredentials(credentials: UserCredentials) {
        dataStore.edit { prefs ->
            prefs[usernameKey] = credentials.username
            prefs[passwordKey] = credentials.password
        }
    }

    suspend fun updateLastSync(timestamp: Long) {
        dataStore.edit { prefs ->
            prefs[lastSyncKey] = timestamp
        }
    }
}
