package com.example.chistopro.di

import com.example.chistopro.data.repository.AuthRepository
import com.example.chistopro.data.repository.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    // Чтобы подключить реальный сервер, замени FakeAuthRepository на новую реализацию
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository
}
