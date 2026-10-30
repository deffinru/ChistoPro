package com.example.chistopro.data.repository

import javax.inject.Inject
import javax.inject.Singleton

/** ВРЕМЕННОЕ хранилище «аккаунта» для заглушек. Исчезнет, когда подключим сервер. */
@Singleton
class FakeAccount @Inject constructor() {
    var email: String = "manager@chistopro.ru"
    var password: String = "manager123"
    var name: String = "Алексей"
}
