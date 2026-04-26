package com.mohamed.servicehub.data

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.PhoneAuthCredential
import dev.gitlive.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class PhoneAuthHelper actual constructor(private val auth: FirebaseAuth) {
    actual suspend fun verifyPhoneNumber(phoneNumber: String): Result<String> = suspendCancellableCoroutine { continuation ->
        PhoneAuthProvider.verifyPhoneNumber(
            phoneNumber = phoneNumber,
            onCodeSent = { verificationId, _ ->
                if (continuation.isActive) continuation.resume(Result.success(verificationId))
            },
            onVerificationFailed = { exception ->
                if (continuation.isActive) continuation.resume(Result.failure(exception))
            }
        )
    }

    actual fun getCredential(verificationId: String, otp: String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId, otp)
    }
}
