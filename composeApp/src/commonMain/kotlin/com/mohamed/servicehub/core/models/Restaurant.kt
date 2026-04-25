package com.mohamed.servicehub.core.models

data class Restaurant(
    val id: String,
    val name: String,
    val cuisine: String,
    val phoneNumber: String,
    val ownerId: String,
    val ownerEmail: String,
    val menu: List<MenuItem>
)
