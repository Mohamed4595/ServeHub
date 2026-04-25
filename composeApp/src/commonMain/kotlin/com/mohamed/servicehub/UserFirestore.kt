package com.mohamed.servicehub
// Mock in-memory Firestore-like storage for user roles.
// This is a placeholder for the real Firestore integration to satisfy
// the requirement to persist user roles after login. The Android/FCM
// integration can provide a real implementation later without changing
// the domain interfaces.

interface UserFirestoreRepository {
    suspend fun saveUserRole(userId: String, role: UserRole): Unit
    suspend fun getUserRole(userId: String): UserRole?
}

/** In-memory implementation used for MVP and tests. */
class InMemoryUserFirestoreRepository : UserFirestoreRepository {
    private val roles = mutableMapOf<String, UserRole>()

    override suspend fun saveUserRole(userId: String, role: UserRole) {
        roles[userId] = role
    }

    override suspend fun getUserRole(userId: String): UserRole? = roles[userId]
}
