package com.mohamed.servicehub.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
