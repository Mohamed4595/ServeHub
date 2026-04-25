package com.mohamed.servicehub.domain.models

data class User(
    val id: String,
    val name: String,
    val role: UserRole
)

enum class UserRole {
    CUSTOMER,
    OWNER
}
