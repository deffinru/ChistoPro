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
        fun order(
            id: Int, address: String, service: String, area: Double, price: Double,
            hour: Int, minute: Int, status: OrderStatus
        ) = Order(
            id = id, address = address, serviceName = service, area = area, totalPrice = price,
            scheduledAt = today.atTime(hour, minute), status = status,
            clientName = "Клиент $id", clientPhone = "+7 900 000-00-0$id"
        )
        listOf(
            order(1, "ул. Ленина, 45, кв. 12", "Генеральная уборка", 120.0, 8400.0, 10, 0, OrderStatus.IN_PROGRESS),
            order(2, "пр. Мира, 10, офис 301", "Офисы и бизнес", 85.0, 6200.0, 13, 30, OrderStatus.NEW),
            order(3, "Садовая, 7, кв. 88", "Стандартная уборка", 210.0, 14500.0, 8, 0, OrderStatus.DONE),
            order(4, "ул. Гагарина, 3, кв. 5", "Стандартная уборка", 60.0, 6600.0, 15, 0, OrderStatus.CONFIRMED),
            order(5, "ул. Победы, 21", "После ремонта", 95.0, 20900.0, 16, 0, OrderStatus.CONFIRMED),
            order(6, "пер. Тихий, 2, кв. 14", "Мойка окон", 40.0, 2400.0, 17, 30, OrderStatus.NEW),
            order(7, "ул. Кирова, 18, кв. 9", "Химчистка мебели", 25.0, 3750.0, 9, 0, OrderStatus.DONE),
            order(8, "ул. Чехова, 4", "Стандартная уборка", 70.0, 7700.0, 9, 30, OrderStatus.DONE),
            order(9, "пр. Ленина, 100, офис 12", "Офисы и бизнес", 150.0, 15000.0, 7, 0, OrderStatus.DONE),
            order(10, "ул. Мичурина, 8, кв. 33", "Генеральная уборка", 55.0, 9900.0, 11, 0, OrderStatus.CANCELLED)
        )
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
