package com.mohamed.servicehub.presentation.loginScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        _root_ide_package_.com.mohamed.servicehub.presentation.components.LogoBadge(size = 88.dp)
        Spacer(modifier = Modifier.height(14.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubWordmark()
        Spacer(modifier = Modifier.height(28.dp))
        Text("Welcome back!", style = MaterialTheme.typography.h5, color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubInk)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Login to continue", color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubMuted)
        Spacer(modifier = Modifier.height(26.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            leading = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubMuted
                )
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            leading = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubMuted
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text("Forgot password?", color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubPrimary, fontSize = 12.sp)
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(errorMessage, color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubError, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.PrimaryActionButton(
            text = if (isLoading) "Signing in..." else "Login",
            onClick = onLogin,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(18.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.OrDivider()
        Spacer(modifier = Modifier.height(18.dp))
        _root_ide_package_.com.mohamed.servicehub.presentation.components.OutlineActionButton(
            text = "Continue with Google",
            onClick = {})
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Use any email. Emails containing owner sign in as OWNER.",
            color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(22.dp))
        Row {
            Text("Don't have an account? ", color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubMuted)
            Text("Sign up", color = _root_ide_package_.com.mohamed.servicehub.presentation.components.ServeHubPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@com.mohamed.servicehub.PhonePreview
@Composable
private fun LoginScreenPreview() = _root_ide_package_.com.mohamed.servicehub.PreviewContainer {
    LoginScreen(
        email = "owner@servehub.app",
        password = "123456",
        isLoading = false,
        errorMessage = null,
        onEmailChange = {},
        onPasswordChange = {},
        onLogin = {}
    )
}

@com.mohamed.servicehub.PhonePreview
@Composable
private fun LoginScreenErrorPreview() = _root_ide_package_.com.mohamed.servicehub.PreviewContainer {
    LoginScreen(
        email = "",
        password = "",
        isLoading = false,
        errorMessage = "Please enter both email and password",
        onEmailChange = {},
        onPasswordChange = {},
        onLogin = {}
    )
}
