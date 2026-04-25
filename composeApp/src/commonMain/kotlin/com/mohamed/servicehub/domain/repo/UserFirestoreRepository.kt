package com.mohamed.servicehub.domain.repo

import com.mohamed.servicehub.presentation.UserRole
interface UserFirestoreRepository {
    suspend fun saveUserRole(userId: String, role: UserRole)
    suspend fun getUserRole(userId: String): UserRole?
}
