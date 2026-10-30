package com.example.chistopro.data.repository

import com.example.chistopro.data.local.TokenStore
import com.example.chistopro.data.remote.ApiService
import com.example.chistopro.data.remote.apiCall
import com.example.chistopro.data.remote.dto.LoginRequest
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenStore
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> =
        apiCall { api.login(LoginRequest(email, password)) }
            .map { response -> tokenStore.saveToken(response.token) }

    override suspend fun logout() {
        tokenStore.clear()
    }
}
