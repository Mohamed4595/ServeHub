package com.mohamed.servicehub.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.servicehub.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneAuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PhoneAuthState())
    val state: StateFlow<PhoneAuthState> = _state.asStateFlow()

    fun onIntent(intent: PhoneAuthIntent) {
        when (intent) {
            is PhoneAuthIntent.UpdatePhone -> _state.update { it.copy(phoneNumber = intent.phone) }
            is PhoneAuthIntent.UpdateOtp -> _state.update { it.copy(otp = intent.otp) }
            PhoneAuthIntent.SendOtp -> sendOtp()
            PhoneAuthIntent.VerifyOtp -> verifyOtp()
            PhoneAuthIntent.Reset -> _state.value = PhoneAuthState()
        }
    }

    private fun sendOtp() {
        var phone = _state.value.phoneNumber
        if (phone.isBlank()) {
            _state.update { it.copy(error = "Please enter phone number") }
            return
        }

        // Basic normalization: Ensure it starts with +
        if (!phone.startsWith("+")) {
            if (phone.startsWith("0")) {
                phone = "+20" + phone.substring(1)
            } else {
                phone = "+" + phone
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.sendOtp(phone)
                .onSuccess { id ->
                    _state.update { it.copy(isLoading = false, verificationId = id, isOtpSent = true) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun verifyOtp() {
        val otp = _state.value.otp
        val id = _state.value.verificationId
        if (otp.isBlank() || id == null) {
            _state.update { it.copy(error = "Please enter OTP") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.verifyOtp(id, otp)
                .onSuccess { session ->
                    _state.update { it.copy(isLoading = false, isCompleted = true, userSession = session) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
