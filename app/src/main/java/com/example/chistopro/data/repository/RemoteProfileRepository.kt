package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Manager
import com.example.chistopro.data.remote.ApiService
import com.example.chistopro.data.remote.apiCall
import com.example.chistopro.data.remote.dto.UpdateProfileRequest
import com.example.chistopro.data.remote.dto.toManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteProfileRepository @Inject constructor(
    private val api: ApiService
) : ProfileRepository {

    private val _manager = MutableStateFlow<Manager?>(null)
    override val manager: StateFlow<Manager?> = _manager.asStateFlow()

    override suspend fun loadManager(): Result<Manager> =
        apiCall { api.getMe() }
            .map { it.toManager() }
            .onSuccess { _manager.value = it }

    override suspend fun updateProfile(
        name: String,
        currentPassword: String?,
        newPassword: String?
    ): Result<Unit> =
        apiCall { api.updateMe(UpdateProfileRequest(name, currentPassword, newPassword)) }
            .map { dto -> _manager.value = dto.toManager() }

    override fun clear() {
        _manager.value = null
    }
}
