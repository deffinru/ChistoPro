package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Employee

interface EmployeesRepository {
    suspend fun getEmployees(): Result<List<Employee>>
}
