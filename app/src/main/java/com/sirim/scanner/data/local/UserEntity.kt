package com.sirim.scanner.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    @ColumnInfo(name = "salt")
    val salt: String,
    @ColumnInfo(name = "biometric_enabled")
    val biometricEnabled: Boolean,
    @ColumnInfo(name = "last_login")
    val lastLogin: Long?,
)
