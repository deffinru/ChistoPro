package com.example.chistopro.data.model

import java.time.LocalDateTime

/**
 * Статус заказа. apiName — значение в таблице Statuses (поле name).
 */
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
 * Заказ (таблица Orders): address, area, scheduled_at, comment, total_price,
 * status_id -> Statuses.name, service_id -> Services.name.
 */
data class Order(
    val id: Int,
    val address: String,
    val serviceName: String,
    val area: Double,
    val totalPrice: Double,
    val scheduledAt: LocalDateTime?,
    val status: OrderStatus,
    val clientName: String = "",
    val clientPhone: String = "",
    val comment: String? = null
)
