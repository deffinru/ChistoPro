package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Employee
import com.example.chistopro.data.remote.ApiService
import com.example.chistopro.data.remote.apiCall
import com.example.chistopro.data.remote.dto.toEmployee
import javax.inject.Inject

class RemoteEmployeesRepository @Inject constructor(
    private val api: ApiService
) : EmployeesRepository {

    override suspend fun getEmployees(): Result<List<Employee>> =
        apiCall { api.getEmployees() }.map { list -> list.map { it.toEmployee() } }
}
