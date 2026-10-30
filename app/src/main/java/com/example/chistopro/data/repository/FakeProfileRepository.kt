package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Manager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** ВРЕМЕННЫЙ профиль управляющего. */
@Singleton
class FakeProfileRepository @Inject constructor(
    private val account: FakeAccount
) : ProfileRepository {

    private val _manager = MutableStateFlow<Manager?>(null)
    override val manager: StateFlow<Manager?> = _manager.asStateFlow()

    override suspend fun loadManager(): Result<Manager> {
        val m = Manager(id = 1, name = account.name, email = account.email)
        _manager.value = m
        return Result.success(m)
    }

    override suspend fun updateProfile(
        name: String,
        currentPassword: String?,
        newPassword: String?
    ): Result<Unit> {
        delay(600)
        if (newPassword != null) {
            if (currentPassword != account.password) {
                return Result.failure(IllegalArgumentException("Неверный текущий пароль"))
            }
            account.password = newPassword
        }
        account.name = name
        _manager.value = Manager(id = 1, name = name, email = account.email)
        return Result.success(Unit)
    }

    override fun clear() {
        _manager.value = null
    }
}
