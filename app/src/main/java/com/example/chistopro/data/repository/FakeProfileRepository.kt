package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Manager
import javax.inject.Inject

/** ВРЕМЕННЫЙ профиль управляющего. */
class FakeProfileRepository @Inject constructor() : ProfileRepository {
    override suspend fun getManager(): Result<Manager> =
        Result.success(Manager(id = 1, name = "Алексей", email = "admin@chistopro.ru"))
}
