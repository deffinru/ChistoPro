package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Order

interface OrdersRepository {
    suspend fun getOrders(): Result<List<Order>>
    suspend fun getOrder(id: Int): Result<Order>
}
