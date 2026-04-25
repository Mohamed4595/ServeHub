package com.mohamed.servicehub.data

import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.domain.repo.UserFirestoreRepository

// Mock in-memory Firestore-like storage for user roles.
// This is a placeholder for the real Firestore integration to satisfy
// the requirement to persist user roles after login. The Android/FCM
// integration can provide a real implementation later without changing
// the domain interfaces.

/** In-memory implementation used for MVP and tests. */
class InMemoryUserFirestoreRepository : UserFirestoreRepository {
    private val roles = mutableMapOf<String, UserRole>()

    override suspend fun saveUserRole(userId: String, role: UserRole) {
        roles[userId] = role
    }

    override suspend fun getUserRole(userId: String): UserRole? = roles[userId]
}
