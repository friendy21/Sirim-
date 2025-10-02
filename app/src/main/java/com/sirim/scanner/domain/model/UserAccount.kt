package com.sirim.scanner.domain.model

data class UserAccount(
    val id: Long,
    val username: String,
    val biometricEnabled: Boolean,
    val lastLogin: Long?,
)
