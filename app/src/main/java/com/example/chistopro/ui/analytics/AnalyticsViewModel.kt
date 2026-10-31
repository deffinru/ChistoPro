package com.example.chistopro.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.WeeklyStats
import com.example.chistopro.data.model.calculateWeeklyStats
import com.example.chistopro.data.repository.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val stats: WeeklyStats? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

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
                        it.copy(isLoading = false, errorMessage = null, stats = calculateWeeklyStats(orders))
                    }
                }
                .onFailure { e ->
                    if (showLoading) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = e.message ?: "Не удалось загрузить данные")
                        }
                    }
                }
        }
    }
}