package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Tariff
import com.example.chistopro.data.remote.ApiService
import com.example.chistopro.data.remote.apiCall
import com.example.chistopro.data.remote.dto.UpdateTariffRequest
import com.example.chistopro.data.remote.dto.toTariff
import javax.inject.Inject

class RemoteTariffsRepository @Inject constructor(
    private val api: ApiService
) : TariffsRepository {

    override suspend fun getTariffs(): Result<List<Tariff>> =
        apiCall { api.getTariffs() }.map { list -> list.map { it.toTariff() } }

    override suspend fun updateTariff(
        id: Int,
        name: String,
        description: String,
        pricePerM2: Double
    ): Result<Tariff> =
        apiCall { api.updateTariff(id, UpdateTariffRequest(name, description, pricePerM2)) }
            .map { it.toTariff() }
}