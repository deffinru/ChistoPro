package com.example.chistopro.di

import com.example.chistopro.data.repository.AuthRepository
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.data.repository.ProfileRepository
import com.example.chistopro.data.repository.RemoteAuthRepository
import com.example.chistopro.data.repository.RemoteOrdersRepository
import com.example.chistopro.data.repository.RemoteProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Какая реализация подставляется во ViewModel.
 * Сейчас — настоящие (через API). Чтобы вернуться к тестовым данным без сервера,
 * замени Remote... на Fake... (классы FakeAuthRepository и др. остались в проекте).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(impl: RemoteAuthRepository): AuthRepository

    @Binds @Singleton
    abstract fun bindOrdersRepository(impl: RemoteOrdersRepository): OrdersRepository

    @Binds @Singleton
    abstract fun bindProfileRepository(impl: RemoteProfileRepository): ProfileRepository
}
