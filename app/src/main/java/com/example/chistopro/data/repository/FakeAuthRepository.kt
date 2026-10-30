package com.example.chistopro.data.repository

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * ВРЕМЕННАЯ реализация (email: manager@chistopro.ru, пароль: manager123 — как в БД).
 * Позже заменяется на реализацию, которая обращается к серверу (REST API).
 */
class FakeAuthRepository @Inject constructor(
    private val account: FakeAccount
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(800)
        return if (email.equals(account.email, ignoreCase = true) && password == account.password) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Неверный email или пароль"))
        }
    }

    override suspend fun logout() {
        // здесь позже будет удаление токена
    }
}
