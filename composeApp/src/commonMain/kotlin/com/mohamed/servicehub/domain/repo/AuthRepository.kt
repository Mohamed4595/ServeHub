package com.mohamed.servicehub.domain.repo

import com.mohamed.servicehub.presentation.UserSession

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Result<String>
    suspend fun verifyOtp(verificationId: String, otp: String): Result<UserSession>
}
