package com.example.chistopro.data.model

/** Сотрудник (пользователь с ролью employee), которого можно назначить на заказ. */
data class Employee(
    val id: Int,
    val name: String,
    val phone: String
)
