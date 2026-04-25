package com.mohamed.servicehub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform