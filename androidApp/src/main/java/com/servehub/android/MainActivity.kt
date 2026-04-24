package com.servehub.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.servehub.shared.RootComponent

class MainActivity : ComponentActivity() {

    private lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootComponent = RootComponent(defaultComponentContext())

        setContent {
            ServeHubTheme {
                ServeHubApp(rootComponent = rootComponent)
            }
        }
    }
}
