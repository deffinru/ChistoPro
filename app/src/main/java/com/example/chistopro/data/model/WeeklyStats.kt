package com.example.chistopro.data.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/** Статистика за календарную неделю (понедельник — воскресенье). */
data class WeeklyStats(
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val ordersCount: Int,
    val revenue: Double,
    val totalArea: Double,
    val averageCheck: Double,
    /** Количество заказов по каждому статусу (для графика). */
    val statusCounts: Map<OrderStatus, Int>
)

/**
 * Считает статистику по заказам текущей недели (по дате выполнения scheduled_at).
 * Заказы, количество, выручка, площадь и средний чек считаются БЕЗ отменённых заказов.
 * График по статусам показывает все статусы, включая отменённые.
 */
fun calculateWeeklyStats(orders: List<Order>, today: LocalDate = LocalDate.now()): WeeklyStats {
    val start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val end = start.plusDays(6)

    val week = orders.filter { order ->
        val date = order.scheduledAt?.toLocalDate()
        date != null && !date.isBefore(start) && !date.isAfter(end)
    }
    val counted = week.filter { it.status != OrderStatus.CANCELLED }
    val revenue = counted.sumOf { it.totalPrice }

    return WeeklyStats(
        weekStart = start,
        weekEnd = end,
        ordersCount = counted.size,
        revenue = revenue,
        totalArea = counted.sumOf { it.area },
        averageCheck = if (counted.isEmpty()) 0.0 else revenue / counted.size,
        statusCounts = OrderStatus.entries.associateWith { status -> week.count { it.status == status } }
    )
}