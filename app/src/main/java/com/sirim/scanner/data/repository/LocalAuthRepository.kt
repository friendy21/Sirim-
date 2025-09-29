package com.sirim.scanner.data.repository

import com.sirim.scanner.data.local.UserDao
import com.sirim.scanner.data.local.UserEntity
import com.sirim.scanner.domain.model.UserAccount
import com.sirim.scanner.domain.repository.AuthRepository
import com.sirim.scanner.util.CredentialsHasher
import com.sirim.scanner.util.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.time.Instant

class LocalAuthRepository(
    private val userDao: UserDao,
    private val dispatchers: DispatchersProvider,
) : AuthRepository {

    private val secureRandom = SecureRandom()

    override suspend fun register(username: String, password: String): Result<Unit> = withContext(dispatchers.io) {
        runCatching {
            val normalized = username.trim().lowercase()
            val existing = userDao.findByUsername(normalized)
            require(existing == null) { "User already exists" }
            val salt = generateSalt()
            val hash = CredentialsHasher.hashPassword(password, salt)
            val entity = UserEntity(
                username = normalized,
                passwordHash = hash,
                salt = salt,
                biometricEnabled = false,
                lastLogin = null
            )
            userDao.insert(entity)
        }
    }

    override suspend fun authenticate(username: String, password: String): Result<UserAccount> = withContext(dispatchers.io) {
        runCatching {
            val normalized = username.trim().lowercase()
            val entity = userDao.findByUsername(normalized) ?: throw IllegalArgumentException("User not found")
            val hash = CredentialsHasher.hashPassword(password, entity.salt)
            if (hash != entity.passwordHash) {
                throw IllegalArgumentException("Invalid credentials")
            }
            val updated = entity.copy(lastLogin = Instant.now().toEpochMilli())
            userDao.update(updated)
            updated.toDomain()
        }
    }

    override suspend fun updateLastLogin(userId: Long) = withContext(dispatchers.io) {
        val entity = userDao.findById(userId) ?: return@withContext
        val updated = entity.copy(lastLogin = Instant.now().toEpochMilli())
        userDao.update(updated)
    }

    override suspend fun setBiometricEnabled(userId: Long, enabled: Boolean) = withContext(dispatchers.io) {
        val entity = userDao.findById(userId) ?: return@withContext
        val updated = entity.copy(biometricEnabled = enabled)
        userDao.update(updated)
    }

    override fun observeCurrentUser(): Flow<UserAccount?> =
        userDao.observeFirstUser().map { entity -> entity?.toDomain() }

    private fun UserEntity.toDomain(): UserAccount = UserAccount(
        id = id,
        username = username,
        biometricEnabled = biometricEnabled,
        lastLogin = lastLogin,
    )

    private fun generateSalt(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }
}
