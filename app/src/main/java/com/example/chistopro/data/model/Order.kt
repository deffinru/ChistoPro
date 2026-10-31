package com.example.chistopro.data.model

import java.time.LocalDateTime

/** Статус заказа. apiName — значение в таблице Statuses (поле name). */
enum class OrderStatus(val apiName: String) {
    NEW("new"),
    CONFIRMED("confirmed"),
    IN_PROGRESS("in_progress"),
    DONE("done"),
    CANCELLED("cancelled");

    /** Активный = ещё не завершён и не отменён. */
    val isActive: Boolean get() = this == NEW || this == CONFIRMED || this == IN_PROGRESS

    companion object {
        fun fromApi(name: String): OrderStatus =
            entries.firstOrNull { it.apiName.equals(name, ignoreCase = true) } ?: NEW
    }
}

/**
 * Заказ (таблица Orders + связанные таблицы):
 * service_id -> Services (тариф), room_type_id -> RoomTypes, user_id -> клиент, emp_id -> сотрудник.
 */
data class Order(
    val id: Int,
    val address: String,
    val serviceName: String,
    val pricePerM2: Double,
    val roomType: String?,
    val area: Double,
    val totalPrice: Double,
    val scheduledAt: LocalDateTime?,
    val status: OrderStatus,
    val clientName: String,
    val clientPhone: String,
    val employeeId: Int?,
    val employeeName: String?,
    val comment: String?
)
