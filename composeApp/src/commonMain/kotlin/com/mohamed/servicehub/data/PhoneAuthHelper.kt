package com.mohamed.servicehub.data

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.PhoneAuthCredential

expect class PhoneAuthHelper(auth: FirebaseAuth) {
    suspend fun verifyPhoneNumber(phoneNumber: String): Result<String>
    fun getCredential(verificationId: String, otp: String): PhoneAuthCredential
}
