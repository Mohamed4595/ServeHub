package com.mohamed.servicehub
import android.app.Application
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class ServeHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin for Android
        startKoin {
            androidContext(this@ServeHubApplication)
            modules(listOf(appKoinModule))
        }
        // Initialize multiplatform DI facade for shared code
        AppDI.init()
    }
}
