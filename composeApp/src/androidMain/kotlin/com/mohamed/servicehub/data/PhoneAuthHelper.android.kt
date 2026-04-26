package com.mohamed.servicehub.data

import com.mohamed.servicehub.MainActivity
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.PhoneAuthCredential
import dev.gitlive.firebase.auth.android
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

actual class PhoneAuthHelper actual constructor(private val auth: FirebaseAuth) {
    actual suspend fun verifyPhoneNumber(phoneNumber: String): Result<String> = suspendCancellableCoroutine { continuation ->
        val activity = MainActivity.instance ?: run {
            if (continuation.isActive) continuation.resume(Result.failure(Exception("Activity not found")))
            return@suspendCancellableCoroutine
        }

        val callbacks = object : com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(verificationId: String, token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken) {
                if (continuation.isActive) continuation.resume(Result.success(verificationId))
            }

            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                // Auto-verification is not handled in this flow which expects a verificationId
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                if (continuation.isActive) continuation.resume(Result.failure(e))
            }
        }

        val options = com.google.firebase.auth.PhoneAuthOptions.newBuilder(auth.android)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        
        com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber(options)
    }

    actual fun getCredential(verificationId: String, otp: String): PhoneAuthCredential {
        val nativeCredential = com.google.firebase.auth.PhoneAuthProvider.getCredential(verificationId, otp)
        return PhoneAuthCredential(nativeCredential)
    }
}
