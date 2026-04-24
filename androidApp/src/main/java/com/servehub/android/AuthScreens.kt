package com.servehub.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFFFF6F0))
                )
            )
    ) {
        FoodPatternBackground()
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

@Composable
internal fun LoginScreen(
    email: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        LogoBadge(size = 88.dp)
        Spacer(modifier = Modifier.height(14.dp))
        ServeHubWordmark()
        Spacer(modifier = Modifier.height(28.dp))
        Text("Welcome back!", style = MaterialTheme.typography.h5, color = ServeHubInk)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Login to continue", color = ServeHubMuted)
        Spacer(modifier = Modifier.height(26.dp))
        ServeHubTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            leading = { Icon(Icons.Default.Email, contentDescription = null, tint = ServeHubMuted) }
        )
        Spacer(modifier = Modifier.height(14.dp))
        ServeHubTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            leading = { Icon(Icons.Default.Lock, contentDescription = null, tint = ServeHubMuted) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text("Forgot password?", color = ServeHubPrimary, fontSize = 12.sp)
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(errorMessage, color = ServeHubError, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryActionButton(
            text = if (isLoading) "Signing in..." else "Login",
            onClick = onLogin,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(18.dp))
        OrDivider()
        Spacer(modifier = Modifier.height(18.dp))
        OutlineActionButton(text = "Continue with Google", onClick = {})
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Use any email. Emails containing owner sign in as OWNER.",
            color = ServeHubMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(22.dp))
        androidx.compose.foundation.layout.Row {
            Text("Don't have an account? ", color = ServeHubMuted)
            Text("Sign up", color = ServeHubPrimary, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FoodPatternBackground() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(6) {
            androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                repeat(4) {
                    Text(
                        text = listOf("🍔", "🍕", "🍜", "🍣", "🌮", "🥗", "🍟", "🥐")[(it + 2) % 8],
                        color = ServeHubPrimary.copy(alpha = 0.18f),
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}
