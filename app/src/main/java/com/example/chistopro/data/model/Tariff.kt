package com.example.chistopro.data.model

/** Тариф = вид уборки (таблица Services). */
data class Tariff(
    val id: Int,
    val name: String,
    val description: String,
    val pricePerM2: Double
)