package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Manager
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    /** Текущий управляющий. Все экраны подписаны на него, поэтому имя обновляется везде сразу. */
    val manager: StateFlow<Manager?>

    suspend fun loadManager(): Result<Manager>

    /**
     * Меняет имя и (если newPassword != null) пароль.
     * Для смены пароля нужен текущий пароль.
     */
    suspend fun updateProfile(
        name: String,
        currentPassword: String?,
        newPassword: String?
    ): Result<Unit>

    /** Очистить данные при выходе. */
    fun clear()
}
