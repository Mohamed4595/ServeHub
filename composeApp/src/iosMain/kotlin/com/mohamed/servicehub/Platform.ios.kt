package com.mohamed.servicehub

import platform.UIKit.UIDevice
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun Double.format(digits: Int): String = NSString.stringWithFormat("%.${digits}f", this)