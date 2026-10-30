package com.example.chistopro.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.data.repository.ProfileRepository
import com.example.chistopro.util.formatFullDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val greeting: String = "",
    val managerName: String = "",
    val avatarLetter: String = "",
    val dateText: String = "",
    /** Количество АКТИВНЫХ заказов (новые, подтверждённые, в работе). */
    val activeCount: Int = 0,
    val orders: List<Order> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // имя обновляется автоматически, если его изменили в профиле
        viewModelScope.launch {
            profileRepository.manager.filterNotNull().collect { m ->
                _uiState.update {
                    it.copy(managerName = m.name, avatarLetter = m.name.take(1).uppercase())
                }
            }
        }
        load()
    }

    fun load() = fetch(showLoading = true)

    /** Тихое обновление без индикатора загрузки (при возврате на экран). */
    fun refresh() {
        if (!_uiState.value.isLoading) fetch(showLoading = false)
    }

    private fun fetch(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val managerResult = profileRepository.loadManager()
            val ordersResult = ordersRepository.getOrders()

            val error = managerResult.exceptionOrNull() ?: ordersResult.exceptionOrNull()
            if (error != null) {
                if (showLoading) {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Не удалось загрузить данные")
                    }
                }
                return@launch
            }

            val orders = ordersResult.getOrThrow()
            val now = LocalDateTime.now()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    greeting = greetingFor(now.hour),
                    dateText = formatFullDate(now),
                    activeCount = orders.count { o -> o.status.isActive },
                    orders = orders.sortedBy { o -> priority(o.status) }.take(HOME_ORDERS_LIMIT)
                )
            }
        }
    }

    private fun priority(status: OrderStatus) = when (status) {
        OrderStatus.IN_PROGRESS -> 0
        OrderStatus.NEW -> 1
        OrderStatus.CONFIRMED -> 2
        OrderStatus.DONE -> 3
        OrderStatus.CANCELLED -> 4
    }

    private fun greetingFor(hour: Int) = when (hour) {
        in 5..11 -> "Доброе утро"
        in 12..17 -> "Добрый день"
        in 18..22 -> "Добрый вечер"
        else -> "Доброй ночи"
    }

    private companion object {
        const val HOME_ORDERS_LIMIT = 3
    }
}
