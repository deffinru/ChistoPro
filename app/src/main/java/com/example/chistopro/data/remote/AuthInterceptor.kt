package com.example.chistopro.data.remote

import com.example.chistopro.data.local.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/** Добавляет токен к каждому запросу и ловит «сессия истекла» (ответ 401). */
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenStore.getToken()
        val request = if (token != null) {
            original.newBuilder().header("Authorization", "Bearer $token").build()
        } else {
            original
        }
        val response = chain.proceed(request)
        if (response.code == 401 && token != null) {
            tokenStore.notifySessionExpired()
        }
        return response
    }
}
