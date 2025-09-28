package com.sirim.qrscanner.core.network.service

import com.sirim.qrscanner.core.network.dto.LoginRequest
import com.sirim.qrscanner.core.network.dto.LoginResponse
import com.sirim.qrscanner.core.network.dto.SirimRecordDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface SirimApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/records")
    suspend fun fetchRecords(@Header("Authorization") token: String): List<SirimRecordDto>

    @PUT("/records")
    suspend fun upsertRecords(
        @Header("Authorization") token: String,
        @Body records: List<SirimRecordDto>
    ): List<SirimRecordDto>
}
