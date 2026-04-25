package com.mohamed.servicehub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginIntent {
    data class UpdateEmail(val value: String) : LoginIntent
    data class UpdatePassword(val value: String) : LoginIntent
    object Submit : LoginIntent
}

sealed interface LoginEffect {
    data class Authenticated(val user: UserSession) : LoginEffect
    data class Failed(val message: String) : LoginEffect
}

private sealed interface LoginResult {
    data class EmailChanged(val value: String) : LoginResult
    data class PasswordChanged(val value: String) : LoginResult
    object Loading : LoginResult
    data class Error(val message: String) : LoginResult
    object Idle : LoginResult
}

private object LoginReducer {
    fun reduce(state: LoginState, result: LoginResult): LoginState =
        when (result) {
            is LoginResult.EmailChanged -> state.copy(email = result.value, errorMessage = null)
            is LoginResult.PasswordChanged -> state.copy(password = result.value, errorMessage = null)
            LoginResult.Loading -> state.copy(isLoading = true, errorMessage = null)
            is LoginResult.Error -> state.copy(isLoading = false, errorMessage = result.message)
            LoginResult.Idle -> state.copy(isLoading = false)
        }
}

class LoginStore(
    private val authRepository: AuthRepository,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects: SharedFlow<LoginEffect> = _effects.asSharedFlow()

    fun dispatch(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateEmail -> reduce(LoginResult.EmailChanged(intent.value))
            is LoginIntent.UpdatePassword -> reduce(LoginResult.PasswordChanged(intent.value))
            LoginIntent.Submit -> submit()
        }
    }

    fun reset() {
        _state.value = LoginState()
    }

    private fun submit() {
        val snapshot = state.value
        if (snapshot.email.isBlank() || snapshot.password.isBlank()) {
            reduce(LoginResult.Error("Please enter both email and password"))
            return
        }

        scope.launch {
            reduce(LoginResult.Loading)
            authRepository.login(snapshot.email, snapshot.password)
                .onSuccess { user ->
                    reduce(LoginResult.Idle)
                    _effects.emit(LoginEffect.Authenticated(user))
                }
                .onFailure { throwable ->
                    val message = throwable.message ?: "Login failed"
                    reduce(LoginResult.Error(message))
                    _effects.emit(LoginEffect.Failed(message))
                }
        }
    }

    private fun reduce(result: LoginResult) {
        _state.update { current ->
            LoginReducer.reduce(current, result)
        }
    }
}
