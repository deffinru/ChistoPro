package com.example.chistopro.data.model

import java.time.LocalDateTime

/** Статус заказа (в БД это таблица Statuses — названия нужно сверить с одногруппником). */
enum class OrderStatus { NEW, IN_PROGRESS, DONE }

/**
 * Заказ. Поля соответствуют таблице Orders:
 * address, area, scheduled_at, comment, total_price, status_id, service -> Services.name.
 * durationHours в БД пока нет — нужно договориться (например, считать от площади).
 */
data class Order(
    val id: Int,
    val address: String,
    val serviceName: String,
    val area: Double,
    val totalPrice: Double,
    val scheduledAt: LocalDateTime,
    val durationHours: Int,
    val status: OrderStatus,
    val clientName: String = "",
    val clientPhone: String = "",
    val comment: String? = null
)
