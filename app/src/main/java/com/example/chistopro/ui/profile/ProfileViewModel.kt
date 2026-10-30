package com.example.chistopro.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.repository.AuthRepository
import com.example.chistopro.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val avatarLetter: String = "",
    val showLogoutDialog: Boolean = false,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // имя обновляется автоматически после редактирования профиля
        viewModelScope.launch {
            profileRepository.manager.filterNotNull().collect { m ->
                _uiState.update {
                    it.copy(name = m.name, avatarLetter = m.name.take(1).uppercase())
                }
            }
        }
        viewModelScope.launch {
            if (profileRepository.manager.value == null) profileRepository.loadManager()
        }
    }

    fun onLogoutClick() = _uiState.update { it.copy(showLogoutDialog = true) }

    fun onLogoutDismiss() = _uiState.update { it.copy(showLogoutDialog = false) }

    fun onLogoutConfirm() {
        viewModelScope.launch {
            authRepository.logout()
            profileRepository.clear()
            _uiState.update { it.copy(showLogoutDialog = false, isLoggedOut = true) }
        }
    }
}
