package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Tariff

interface TariffsRepository {
    suspend fun getTariffs(): Result<List<Tariff>>

    /** Сохраняет изменения тарифа в БД и возвращает обновлённый тариф. */
    suspend fun updateTariff(id: Int, name: String, description: String, pricePerM2: Double): Result<Tariff>
}