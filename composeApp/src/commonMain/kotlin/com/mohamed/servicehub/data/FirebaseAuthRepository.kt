package com.mohamed.servicehub.data

import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.presentation.UserSession
import com.mohamed.servicehub.domain.repo.AuthRepository
import com.mohamed.servicehub.domain.repo.UserFirestoreRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRoleStore: UserFirestoreRepository,
    private val phoneAuthHelper: PhoneAuthHelper,
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Result<String> {
        return phoneAuthHelper.verifyPhoneNumber(phoneNumber)
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<UserSession> {
        return try {
            val credential = phoneAuthHelper.getCredential(verificationId, otp)
            val authResult = firebaseAuth.signInWithCredential(credential)
            val user = authResult.user ?: throw Exception("Auth failed")
            fetchUserAndCreateIfMissing(user.uid, null, user.phoneNumber)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchUserAndCreateIfMissing(
        userId: String,
        email: String?,
        phone: String?
    ): Result<UserSession> {
        return try {
            val doc = firestore.collection("users").document(userId).get()
            val session = if (doc.exists) {
                val roleStr = try { doc.get<String>("role") } catch (_: Exception) { null }
                val role = UserRole.entries.find { it.name == roleStr } ?: UserRole.CUSTOMER
                UserSession(id = userId, email = email, phoneNumber = phone, role = role)
            } else {
                // New User: Create record
                val newUser = mapOf(
                    "id" to userId,
                    "phone" to phone,
                    "email" to email,
                    "role" to UserRole.CUSTOMER.name,
                    "createdAt" to dev.gitlive.firebase.firestore.FieldValue.serverTimestamp
                )
                firestore.collection("users").document(userId).set(newUser)
                UserSession(id = userId, email = email, phoneNumber = phone, role = UserRole.CUSTOMER)
            }
            saveRoleSafely(userId, session.role)
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveRoleSafely(userId: String, role: UserRole) {
        try {
            userRoleStore.saveUserRole(userId, role)
            firestore.collection("users").document(userId).set(mapOf("role" to role.name), merge = true)
        } catch (e: Exception) { }
    }
}