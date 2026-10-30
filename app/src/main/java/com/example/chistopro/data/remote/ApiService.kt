package com.example.chistopro.data.remote

import com.example.chistopro.data.remote.dto.LoginRequest
import com.example.chistopro.data.remote.dto.LoginResponse
import com.example.chistopro.data.remote.dto.ManagerDto
import com.example.chistopro.data.remote.dto.OrderDto
import com.example.chistopro.data.remote.dto.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/** Описание запросов к API (адреса совпадают с серверной частью). */
interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("api/me")
    suspend fun getMe(): ManagerDto

    @PATCH("api/me")
    suspend fun updateMe(@Body body: UpdateProfileRequest): ManagerDto

    @GET("api/orders")
    suspend fun getOrders(): List<OrderDto>

    @GET("api/orders/{id}")
    suspend fun getOrder(@Path("id") id: Int): OrderDto
}
