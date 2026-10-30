package com.example.chistopro.data.repository

/** Контракт авторизации. ViewModel знает только об этом интерфейсе. */
interface AuthRepository {
    /** Result.success при верных данных, иначе Result.failure с текстом ошибки. */
    suspend fun login(email: String, password: String): Result<Unit>

    /** Выход из аккаунта (позже — удаление токена). */
    suspend fun logout()
}
