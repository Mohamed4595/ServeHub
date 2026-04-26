package com.mohamed.servicehub.presentation.auth

import com.mohamed.servicehub.presentation.UserSession

data class PhoneAuthState(
    val phoneNumber: String = "",
    val otp: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOtpSent: Boolean = false,
    val isCompleted: Boolean = false,
    val userSession: UserSession? = null
)

sealed interface PhoneAuthIntent {
    data class UpdatePhone(val phone: String) : PhoneAuthIntent
    data class UpdateOtp(val otp: String) : PhoneAuthIntent
    object SendOtp : PhoneAuthIntent
    object VerifyOtp : PhoneAuthIntent
    object Reset : PhoneAuthIntent
}
