package com.example.chistopro.data.remote.dto

import com.example.chistopro.data.model.Tariff
import com.example.chistopro.data.model.Employee
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

data class UpdateStatusRequest(val status: String)

/** employeeId = null -> поле не отправляется -> сервер снимает сотрудника с заказа. */
data class AssignEmployeeRequest(val employeeId: Int?)

// ---- ответы ----
data class ManagerDto(val id: Int, val name: String, val email: String)

data class LoginResponse(val token: String, val manager: ManagerDto)

data class OrderDto(
    val id: Int,
    val address: String?,
    val serviceName: String?,
    val pricePerM2: Double?,
    val roomType: String?,
    val area: Double?,
    val totalPrice: Double?,
    val scheduledAt: String?,
    val createdAt: String?,
    val status: String?,
    val clientName: String?,
    val clientPhone: String?,
    val employeeId: Int?,
    val employeeName: String?,
    val comment: String?
)

data class EmployeeDto(val id: Int, val name: String?, val phone: String?)

data class ErrorResponse(val error: String?)

// ---- преобразование в модели приложения ----
fun ManagerDto.toManager() = Manager(id = id, name = name, email = email)

fun EmployeeDto.toEmployee() = Employee(id = id, name = name.orEmpty(), phone = phone.orEmpty())

fun OrderDto.toOrder() = Order(
    id = id,
    address = address.orEmpty(),
    serviceName = serviceName.orEmpty(),
    pricePerM2 = pricePerM2 ?: 0.0,
    roomType = roomType,
    area = area ?: 0.0,
    totalPrice = totalPrice ?: 0.0,
    scheduledAt = scheduledAt?.let { runCatching { LocalDateTime.parse(it) }.getOrNull() },
    status = OrderStatus.fromApi(status.orEmpty()),
    clientName = clientName.orEmpty(),
    clientPhone = clientPhone.orEmpty(),
    employeeId = employeeId,
    employeeName = employeeName,
    comment = comment
)

data class TariffDto(
    val id: Int,
    val name: String?,
    val description: String?,
    val pricePerM2: Double?
)

data class UpdateTariffRequest(
    val name: String,
    val description: String,
    val pricePerM2: Double
)

fun TariffDto.toTariff() = Tariff(
    id = id,
    name = name.orEmpty(),
    description = description.orEmpty(),
    pricePerM2 = pricePerM2 ?: 0.0
)
