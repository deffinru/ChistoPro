package com.example.chistopro.data.repository

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * ВРЕМЕННАЯ реализация (email: admin@chistopro.ru, пароль: admin).
 * Позже заменяется на реализацию, которая обращается к серверу (REST API).
 */
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(800)
        return if (email.equals("admin@chistopro.ru", ignoreCase = true) && password == "admin") {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Неверный email или пароль"))
        }
    }
}
