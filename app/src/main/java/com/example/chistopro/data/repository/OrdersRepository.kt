package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus

interface OrdersRepository {
    suspend fun getOrders(): Result<List<Order>>
    suspend fun getOrder(id: Int): Result<Order>

    /** Меняет статус заказа в БД и возвращает обновлённый заказ. */
    suspend fun updateStatus(id: Int, status: OrderStatus): Result<Order>

    /** Назначает сотрудника на заказ (employeeId = null — снять сотрудника). */
    suspend fun assignEmployee(id: Int, employeeId: Int?): Result<Order>
}
