package com.mohamed.servicehub

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun Double.format(digits: Int): String = "%.${digits}f".format(this)