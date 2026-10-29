package com.example.chistopro.di

import com.example.chistopro.data.repository.AuthRepository
import com.example.chistopro.data.repository.FakeAuthRepository
import com.example.chistopro.data.repository.FakeOrdersRepository
import com.example.chistopro.data.repository.FakeProfileRepository
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.data.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Здесь выбирается, какая реализация подставляется во ViewModel.
 * Когда появится API — замени Fake... на реальные реализации.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository

    @Binds @Singleton
    abstract fun bindOrdersRepository(impl: FakeOrdersRepository): OrdersRepository

    @Binds @Singleton
    abstract fun bindProfileRepository(impl: FakeProfileRepository): ProfileRepository
}
