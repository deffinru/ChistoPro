package com.example.chistopro.data.repository

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * ВРЕМЕННАЯ реализация для разработки интерфейса (логин: admin, пароль: admin).
 * Позже заменяется на реализацию, которая обращается к серверу (REST API).
 */
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun login(login: String, password: String): Result<Unit> {
        delay(800) // имитация сетевого запроса
        return if (login == "admin" && password == "admin") {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Неверный логин или пароль"))
        }
    }
}
