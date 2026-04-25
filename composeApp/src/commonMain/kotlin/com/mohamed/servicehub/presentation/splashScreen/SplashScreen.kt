package com.mohamed.servicehub.presentation.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.components.LogoBadge
import com.mohamed.servicehub.PhonePreview
import com.mohamed.servicehub.PreviewContainer
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubWordmark
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import servehub.composeapp.generated.resources.Res
import servehub.composeapp.generated.resources.splash_background

@Composable
internal fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(Res.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoBadge(size = 104.dp)
            Spacer(modifier = Modifier.height(20.dp))
            ServeHubWordmark()
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Your Restaurant.\nYour Way.",
                color = ServeHubInk,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )
        }
    }
}

@PhonePreview
@Composable
private fun SplashScreenPreview() = PreviewContainer {
    SplashScreen(onFinished = {})
}
