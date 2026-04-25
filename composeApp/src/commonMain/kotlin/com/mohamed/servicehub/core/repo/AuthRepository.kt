package com.mohamed.servicehub.core.repo

import com.mohamed.servicehub.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}
