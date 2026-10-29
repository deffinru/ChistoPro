package com.example.chistopro.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, errorMessage = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, errorMessage = null) }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.isLoading) return

        val email = state.email.trim()
        if (email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите email и пароль") }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Введите корректный email") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.login(email, state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Не удалось выполнить вход"
                        )
                    }
                }
        }
    }
}
