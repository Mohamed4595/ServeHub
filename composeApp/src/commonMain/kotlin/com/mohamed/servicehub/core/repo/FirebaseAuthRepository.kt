package com.mohamed.servicehub.core.repo

import com.mohamed.servicehub.UserRole
import com.mohamed.servicehub.UserSession
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepository constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRoleStore: UserFirestoreRepository
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<UserSession> {
        return suspendCancellableCoroutine { cont ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        val userId = firebaseUser?.uid ?: email
                        // Read role from users/{userId} collection
                        val docRef = firestore.collection("users").document(userId)
                        docRef.get()
                            .addOnSuccessListener { doc ->
                                val roleStr = doc.getString("role") ?: "CUSTOMER"
                                val role = if (roleStr.equals(
                                        "OWNER",
                                        true
                                    )
                                ) UserRole.OWNER else UserRole.CUSTOMER
                                val session = UserSession(id = userId, email = email, role = role)
                                // Persist role for future retrieval (via Firestore)
                                scopeSafeSaveRole(userId, role)
                                cont.resume(Result.success(session))
                            }
                            .addOnFailureListener { e -> cont.resumeWithException(e) }
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("Login failed")))
                    }
                }
        }
    }

    private fun scopeSafeSaveRole(userId: String, role: UserRole) {
        // Fire-and-forget: best-effort save
        try {
            firestore.collection("users").document(userId).set(mapOf("role" to role.name))
        } catch (_: Throwable) { /* ignore */ }
    }
}