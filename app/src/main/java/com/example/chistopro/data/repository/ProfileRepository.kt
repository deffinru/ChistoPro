package com.example.chistopro.data.repository

import com.example.chistopro.data.model.Manager

interface ProfileRepository {
    suspend fun getManager(): Result<Manager>
}
