package com.example.chistopro.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val greeting: String = "",
    val managerName: String = "",
    val avatarLetter: String = "",
    val dateText: String = "",
    val ordersCount: Int = 0,
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
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val managerResult = profileRepository.getManager()
            val ordersResult = ordersRepository.getOrders()

            val error = managerResult.exceptionOrNull() ?: ordersResult.exceptionOrNull()
            if (error != null) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Не удалось загрузить данные")
                }
                return@launch
            }

            val manager = managerResult.getOrThrow()
            val orders = ordersResult.getOrThrow()
            val now = LocalDateTime.now()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    greeting = greetingFor(now.hour),
                    managerName = manager.name,
                    avatarLetter = manager.name.take(1).uppercase(),
                    dateText = formatDate(now),
                    ordersCount = orders.size,
                    // на главной: сначала в работе, затем новые, затем выполненные; показываем 3
                    orders = orders.sortedBy { o -> priority(o.status) }.take(HOME_ORDERS_LIMIT)
                )
            }
        }
    }

    private fun priority(status: OrderStatus) = when (status) {
        OrderStatus.IN_PROGRESS -> 0
        OrderStatus.NEW -> 1
        OrderStatus.DONE -> 2
    }

    private fun greetingFor(hour: Int) = when (hour) {
        in 5..11 -> "Доброе утро"
        in 12..17 -> "Добрый день"
        in 18..22 -> "Добрый вечер"
        else -> "Доброй ночи"
    }

    private fun formatDate(dateTime: LocalDateTime): String =
        dateTime.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.forLanguageTag("ru")))
            .replaceFirstChar { it.uppercase() }

    private companion object {
        const val HOME_ORDERS_LIMIT = 3
    }
}
