package com.mohamed.servicehub.core.repo

import com.mohamed.servicehub.UserRole
interface UserFirestoreRepository {
    suspend fun saveUserRole(userId: String, role: UserRole)
    suspend fun getUserRole(userId: String): UserRole?
}
