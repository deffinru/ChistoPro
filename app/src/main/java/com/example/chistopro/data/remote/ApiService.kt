package com.example.chistopro.data.remote

import com.example.chistopro.data.remote.dto.AssignEmployeeRequest
import com.example.chistopro.data.remote.dto.EmployeeDto
import com.example.chistopro.data.remote.dto.LoginRequest
import com.example.chistopro.data.remote.dto.LoginResponse
import com.example.chistopro.data.remote.dto.ManagerDto
import com.example.chistopro.data.remote.dto.OrderDto
import com.example.chistopro.data.remote.dto.TariffDto
import com.example.chistopro.data.remote.dto.UpdateProfileRequest
import com.example.chistopro.data.remote.dto.UpdateStatusRequest
import com.example.chistopro.data.remote.dto.UpdateTariffRequest
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

    @PATCH("api/orders/{id}/status")
    suspend fun updateOrderStatus(@Path("id") id: Int, @Body body: UpdateStatusRequest): OrderDto

    @PATCH("api/orders/{id}/employee")
    suspend fun assignEmployee(@Path("id") id: Int, @Body body: AssignEmployeeRequest): OrderDto

    @GET("api/employees")
    suspend fun getEmployees(): List<EmployeeDto>

    @GET("api/services")
    suspend fun getTariffs(): List<TariffDto>

    @PATCH("api/services/{id}")
    suspend fun updateTariff(@Path("id") id: Int, @Body body: UpdateTariffRequest): TariffDto
}