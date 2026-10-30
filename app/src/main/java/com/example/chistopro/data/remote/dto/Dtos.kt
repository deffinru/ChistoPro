package com.example.chistopro.data.remote.dto

import com.example.chistopro.data.model.Manager
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import java.time.LocalDateTime

// ---- запросы ----
data class LoginRequest(val email: String, val password: String)

/** null-поля в JSON не отправляются, сервер получает только заполненные. */
data class UpdateProfileRequest(
    val name: String?,
    val currentPassword: String?,
    val newPassword: String?
)

// ---- ответы ----
data class ManagerDto(val id: Int, val name: String, val email: String)

data class LoginResponse(val token: String, val manager: ManagerDto)

data class OrderDto(
    val id: Int,
    val address: String?,
    val serviceName: String?,
    val area: Double?,
    val totalPrice: Double?,
    val scheduledAt: String?,
    val status: String?,
    val clientName: String?,
    val clientPhone: String?,
    val comment: String?
)

data class ErrorResponse(val error: String?)

// ---- преобразование в модели приложения ----
fun ManagerDto.toManager() = Manager(id = id, name = name, email = email)

fun OrderDto.toOrder() = Order(
    id = id,
    address = address.orEmpty(),
    serviceName = serviceName.orEmpty(),
    area = area ?: 0.0,
    totalPrice = totalPrice ?: 0.0,
    scheduledAt = scheduledAt?.let { runCatching { LocalDateTime.parse(it) }.getOrNull() },
    status = OrderStatus.fromApi(status.orEmpty()),
    clientName = clientName.orEmpty(),
    clientPhone = clientPhone.orEmpty(),
    comment = comment
)
