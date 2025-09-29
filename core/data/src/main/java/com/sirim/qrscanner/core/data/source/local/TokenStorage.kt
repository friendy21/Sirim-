package com.sirim.qrscanner.core.data.source.local

import android.content.SharedPreferences
import at.favre.lib.crypto.bcrypt.BCrypt
import com.sirim.qrscanner.core.domain.model.UserCredentials
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class TokenStorage(
    private val securePreferences: SharedPreferences
) {

    private val tokenKey = "auth_token"
    private val usernameKey = "username"
    private val passwordKey = "password"
    private val hashedPasswordKey = "password_hash"
    private val lastSyncKey = "last_sync"

    private val tokenState = MutableStateFlow(securePreferences.getString(tokenKey, null))
    private val credentialsState = MutableStateFlow(readCredentials())

    private val listenerRegistered = AtomicBoolean(false)

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {
            tokenKey -> tokenState.value = prefs.getString(tokenKey, null)
            usernameKey, passwordKey -> credentialsState.value = readCredentials()
        }
    }

    private fun ensureListener() {
        if (listenerRegistered.compareAndSet(false, true)) {
            securePreferences.registerOnSharedPreferenceChangeListener(listener)
        }
    }

    val token: Flow<String?>
        get() {
            ensureListener()
            return tokenState
        }

    fun credentials(): Flow<UserCredentials?> {
        ensureListener()
        return credentialsState
    }

    fun lastSync(): Flow<Long?> = callbackFlow {
        ensureListener()
        trySend(securePreferences.getLong(lastSyncKey, -1L).takeIf { it >= 0 })
        val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == lastSyncKey) {
                val value = prefs.getLong(lastSyncKey, -1L).takeIf { it >= 0 }
                trySend(value)
            }
        }
        securePreferences.registerOnSharedPreferenceChangeListener(prefListener)
        awaitClose { securePreferences.unregisterOnSharedPreferenceChangeListener(prefListener) }
    }

    suspend fun saveToken(token: String) {
        securePreferences.edit().putString(tokenKey, token).apply()
        tokenState.value = token
    }

    suspend fun clearToken() {
        securePreferences.edit().remove(tokenKey).apply()
        tokenState.value = null
    }

    suspend fun updateCredentials(credentials: UserCredentials) {
        if (credentials.username.isBlank() || credentials.password.isBlank()) {
            securePreferences.edit()
                .remove(usernameKey)
                .remove(passwordKey)
                .remove(hashedPasswordKey)
                .apply()
            credentialsState.value = null
        } else {
            securePreferences.edit()
                .putString(usernameKey, credentials.username)
                .putString(passwordKey, credentials.password)
                .putString(
                    hashedPasswordKey,
                    BCrypt.withDefaults().hashToString(12, credentials.password.toCharArray())
                )
                .apply()
            credentialsState.value = credentials
        }
    }

    suspend fun updateLastSync(timestamp: Long) {
        securePreferences.edit().putLong(lastSyncKey, timestamp).apply()
    }

    fun hasherDigest(): Flow<String?> = credentials().map { _ ->
        securePreferences.getString(hashedPasswordKey, null)
    }

    fun verifyPassword(candidate: String): Boolean {
        val storedHash = securePreferences.getString(hashedPasswordKey, null) ?: return false
        return BCrypt.verifyer().verify(candidate.toCharArray(), storedHash.toCharArray()).verified
    }

    private fun readCredentials(): UserCredentials? {
        val username = securePreferences.getString(usernameKey, null)
        val password = securePreferences.getString(passwordKey, null)
        return if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
            UserCredentials(username, password)
        } else {
            null
        }
    }
}
