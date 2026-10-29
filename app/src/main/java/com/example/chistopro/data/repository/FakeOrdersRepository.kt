package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

/** ВРЕМЕННЫЕ тестовые заказы. Позже заменяется на загрузку с сервера. */
class FakeOrdersRepository @Inject constructor() : OrdersRepository {

    private val orders: List<Order> by lazy {
        val today = LocalDate.now()
        val main = listOf(
            Order(
                id = 1, address = "ул. Ленина, 45, кв. 12", serviceName = "Поддерживающая",
                area = 54.0, totalPrice = 4320.0, scheduledAt = today.atTime(10, 0),
                durationHours = 3, status = OrderStatus.IN_PROGRESS,
                clientName = "Иван Петров", clientPhone = "+7 900 111-22-33"
            ),
            Order(
                id = 2, address = "пр. Мира, 10, офис 301", serviceName = "Генеральная",
                area = 80.0, totalPrice = 9600.0, scheduledAt = today.atTime(13, 30),
                durationHours = 4, status = OrderStatus.NEW,
                clientName = "ООО «Ромашка»", clientPhone = "+7 900 222-33-44"
            ),
            Order(
                id = 3, address = "Садовая, 7, кв. 88", serviceName = "Поддерживающая",
                area = 40.0, totalPrice = 3200.0, scheduledAt = today.atTime(8, 0),
                durationHours = 2, status = OrderStatus.DONE,
                clientName = "Мария Смирнова", clientPhone = "+7 900 333-44-55"
            )
        )
        // ещё 15 выполненных заказов, чтобы счётчик показывал 18, как на макете
        val extra = (4..18).map { i ->
            Order(
                id = i, address = "Тестовая ул., $i", serviceName = "Поддерживающая",
                area = 30.0 + i, totalPrice = 2500.0 + i * 50, scheduledAt = today.atTime(7, 0),
                durationHours = 2, status = OrderStatus.DONE
            )
        }
        main + extra
    }

    override suspend fun getOrders(): Result<List<Order>> {
        delay(500)
        return Result.success(orders)
    }

    override suspend fun getOrder(id: Int): Result<Order> {
        delay(300)
        return orders.firstOrNull { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Заказ не найден"))
    }
}
