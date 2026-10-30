package com.example.chistopro.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Хранит токен входа на телефоне, чтобы не вводить пароль при каждом запуске. */
@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("chistopro_session", Context.MODE_PRIVATE)

    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    /** Срабатывает, когда сервер сообщил, что токен недействителен. */
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun notifySessionExpired() {
        clear()
        _sessionExpired.tryEmit(Unit)
    }

    private companion object {
        const val KEY_TOKEN = "token"
    }
}
