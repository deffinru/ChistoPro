package com.example.chistopro.ui.orderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.Employee
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.data.repository.EmployeesRepository
import com.example.chistopro.data.repository.OrdersRepository
import com.example.chistopro.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderDetailsUiState(
    val isLoading: Boolean = true,
    val order: Order? = null,
    val employees: List<Employee> = emptyList(),
    /** Идёт сохранение изменения в БД. */
    val isSaving: Boolean = false,
    /** Ошибка загрузки заказа (показывается вместо страницы). */
    val errorMessage: String? = null,
    /** Ошибка смены статуса/сотрудника (показывается под карточкой). */
    val actionError: String? = null,
    val showEmployeeDialog: Boolean = false,
    val showCancelDialog: Boolean = false
)

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val ordersRepository: OrdersRepository,
    private val employeesRepository: EmployeesRepository
) : ViewModel() {

    // номер заказа приходит из навигации (route "order/{orderId}")
    private val orderId: Int = savedStateHandle.get<Int>(Routes.ORDER_ID_ARG) ?: 0

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val orderResult = ordersRepository.getOrder(orderId)
            // если список сотрудников не загрузился — страница всё равно открывается
            val employees = employeesRepository.getEmployees().getOrDefault(emptyList())

            orderResult
                .onSuccess { order ->
                    _uiState.update { it.copy(isLoading = false, order = order, employees = employees) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Не удалось загрузить заказ")
                    }
                }
        }
    }

    // ---------- статус ----------
    fun onStatusClick(status: OrderStatus) {
        val state = _uiState.value
        if (state.isSaving || state.order?.status == status) return
        if (status == OrderStatus.CANCELLED) {
            _uiState.update { it.copy(showCancelDialog = true) } // отмену подтверждаем
        } else {
            changeStatus(status)
        }
    }

    fun onCancelConfirm() {
        _uiState.update { it.copy(showCancelDialog = false) }
        changeStatus(OrderStatus.CANCELLED)
    }

    fun onCancelDismiss() = _uiState.update { it.copy(showCancelDialog = false) }

    private fun changeStatus(status: OrderStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, actionError = null) }
            ordersRepository.updateStatus(orderId, status)
                .onSuccess { updated ->
                    _uiState.update { it.copy(isSaving = false, order = updated) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, actionError = e.message ?: "Не удалось изменить статус")
                    }
                }
        }
    }

    // ---------- сотрудник ----------
    fun onEmployeeCardClick() = _uiState.update { it.copy(showEmployeeDialog = true) }

    fun onEmployeeDialogDismiss() = _uiState.update { it.copy(showEmployeeDialog = false) }

    /** employee = null — снять сотрудника с заказа. */
    fun onEmployeeSelected(employee: Employee?) {
        val state = _uiState.value
        _uiState.update { it.copy(showEmployeeDialog = false) }
        if (state.isSaving || state.order?.employeeId == employee?.id) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, actionError = null) }
            ordersRepository.assignEmployee(orderId, employee?.id)
                .onSuccess { updated ->
                    _uiState.update { it.copy(isSaving = false, order = updated) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, actionError = e.message ?: "Не удалось назначить сотрудника")
                    }
                }
        }
    }
}
