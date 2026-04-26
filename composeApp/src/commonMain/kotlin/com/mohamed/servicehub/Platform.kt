package com.mohamed.servicehub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun Double.format(digits: Int): String