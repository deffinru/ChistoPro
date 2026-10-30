package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Order
import com.example.chistopro.data.remote.ApiService
import com.example.chistopro.data.remote.apiCall
import com.example.chistopro.data.remote.dto.toOrder
import javax.inject.Inject

class RemoteOrdersRepository @Inject constructor(
    private val api: ApiService
) : OrdersRepository {

    override suspend fun getOrders(): Result<List<Order>> =
        apiCall { api.getOrders() }.map { list -> list.map { it.toOrder() } }

    override suspend fun getOrder(id: Int): Result<Order> =
        apiCall { api.getOrder(id) }.map { it.toOrder() }
}
