package com.example.chistopro.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.util.formatFullDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class OrdersUiState(
    val isLoading: Boolean = true,
    val dateText: String = "",
    val orders: List<Order> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState(dateText = formatFullDate(LocalDateTime.now())))
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
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
            ordersRepository.getOrders()
                .onSuccess { orders ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            // сначала активные, внутри группы — по времени
                            orders = orders.sortedWith(
                                compareBy<Order>({ o -> if (o.status.isActive) 0 else 1 }, { o -> o.scheduledAt })
                            )
                        )
                    }
                }
                .onFailure { e ->
                    if (showLoading) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = e.message ?: "Не удалось загрузить заказы")
                        }
                    }
                }
        }
    }
}
