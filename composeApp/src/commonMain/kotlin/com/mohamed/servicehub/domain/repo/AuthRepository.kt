package com.mohamed.servicehub.domain.repo

import com.mohamed.servicehub.presentation.UserSession

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserSession>
}
