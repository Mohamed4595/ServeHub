package com.mohamed.servicehub
import android.app.Application
import com.mohamed.servicehub.di.appModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class ServeHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ServeHubApplication)
            modules(appModule)
        }
    }
}
