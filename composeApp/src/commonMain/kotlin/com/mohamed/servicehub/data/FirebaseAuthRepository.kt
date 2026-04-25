package com.mohamed.servicehub.data

import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.presentation.UserSession
import com.mohamed.servicehub.domain.repo.AuthRepository
import com.mohamed.servicehub.domain.repo.UserFirestoreRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRoleStore: UserFirestoreRepository
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserSession> {
        return try {
            // GitLive Firebase methods are suspend functions.
            // We can call them directly in this suspend function.
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
            val userId = authResult.user?.uid ?: firebaseAuth.currentUser?.uid ?: email

            // Read role from users/{userId} collection
            val doc = firestore.collection("users").document(userId).get()

            val roleStr = if (doc.exists) {
                try {
                    doc.get<String>("role")
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }

            val role = if (roleStr?.equals("OWNER", true) == true) {
                UserRole.OWNER
            } else {
                UserRole.CUSTOMER
            }

            val session = UserSession(id = userId, email = email, role = role)

            // Persist role for future retrieval
            saveRoleSafely(userId, role)

            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveRoleSafely(userId: String, role: UserRole) {
        try {
            // Update both the specific userRoleStore and the general firestore for consistency
            userRoleStore.saveUserRole(userId, role)
            firestore.collection("users").document(userId).set(mapOf("role" to role.name))
        } catch (e: Exception) {
            // Best-effort: ignore errors during background-like persistence
        }
    }
}