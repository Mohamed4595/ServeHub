package com.mohamed.servicehub

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mohamed.servicehub.presentation.ServeHubApp
import com.mohamed.servicehub.presentation.ServeHubViewModel
import com.mohamed.servicehub.presentation.theme.ServeHubTheme
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        ServeHubTheme {
            val viewModel: ServeHubViewModel = koinViewModel()
            ServeHubApp(viewModel = viewModel)
        }
    }
}