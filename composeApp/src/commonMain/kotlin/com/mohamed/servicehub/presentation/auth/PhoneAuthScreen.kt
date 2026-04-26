package com.mohamed.servicehub.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.servicehub.presentation.UserRole
import com.mohamed.servicehub.presentation.components.PrimaryActionButton
import com.mohamed.servicehub.presentation.components.ServeHubError
import com.mohamed.servicehub.presentation.components.ServeHubInk
import com.mohamed.servicehub.presentation.components.ServeHubMuted
import com.mohamed.servicehub.presentation.components.ServeHubTextField

@Composable
fun PhoneAuthScreen(
    viewModel: PhoneAuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            val role = state.userSession?.role ?: UserRole.CUSTOMER
            when (role) {
                UserRole.CUSTOMER -> onNavigateToHome()
                UserRole.STAFF -> onNavigateToStaff() // Assuming staff/owner
                UserRole.STAFF -> onNavigateToStaff()
                UserRole.ADMIN -> onNavigateToAdmin()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (!state.isOtpSent) "Phone Login" else "Verify OTP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ServeHubInk
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (!state.isOtpSent) "Enter your phone number to continue" else "Enter the 6-digit code sent to your phone",
            color = ServeHubMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!state.isOtpSent) {
            ServeHubTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.onIntent(PhoneAuthIntent.UpdatePhone(it)) },
                label = "Phone Number (+1...)"
            )
        } else {
            ServeHubTextField(
                value = state.otp,
                onValueChange = { viewModel.onIntent(PhoneAuthIntent.UpdateOtp(it)) },
                label = "6-digit OTP"
            )
        }

        if (state.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(state.error!!, color = ServeHubError, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryActionButton(
            text = if (state.isLoading) "Processing..." else if (!state.isOtpSent) "Send OTP" else "Verify OTP",
            onClick = {
                if (!state.isOtpSent) viewModel.onIntent(PhoneAuthIntent.SendOtp)
                else viewModel.onIntent(PhoneAuthIntent.VerifyOtp)
            },
            enabled = !state.isLoading
        )

        if (state.isOtpSent) {
            TextButton(onClick = { viewModel.onIntent(PhoneAuthIntent.Reset) }) {
                Text("Change Phone Number", color = ServeHubMuted)
            }
        }
    }
}

@Composable
private fun TextButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    androidx.compose.material.TextButton(onClick = onClick, content = { content() })
}
