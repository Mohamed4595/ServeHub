package com.mohamed.servicehub

import androidx.compose.ui.window.ComposeUIViewController
import com.mohamed.servicehub.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }