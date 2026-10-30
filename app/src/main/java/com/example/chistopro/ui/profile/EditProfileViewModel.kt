package com.example.chistopro.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val name: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditProfileUiState(name = profileRepository.manager.value?.name.orEmpty())
    )
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
    fun onCurrentPasswordChange(v: String) = _uiState.update { it.copy(currentPassword = v, errorMessage = null) }
    fun onNewPasswordChange(v: String) = _uiState.update { it.copy(newPassword = v, errorMessage = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }

    fun onSaveClick() {
        val s = _uiState.value
        if (s.isLoading) return

        val name = s.name.trim()
        val error = when {
            name.isBlank() -> "Введите имя"
            s.newPassword.isNotEmpty() && s.currentPassword.isEmpty() -> "Введите текущий пароль"
            s.newPassword.isNotEmpty() && s.newPassword.length < MIN_PASSWORD_LENGTH ->
                "Новый пароль должен содержать минимум $MIN_PASSWORD_LENGTH символов"
            s.newPassword != s.confirmPassword -> "Пароли не совпадают"
            else -> null
        }
        if (error != null) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            profileRepository.updateProfile(
                name = name,
                currentPassword = s.currentPassword.ifEmpty { null },
                newPassword = s.newPassword.ifEmpty { null }
            )
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Не удалось сохранить")
                    }
                }
        }
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
