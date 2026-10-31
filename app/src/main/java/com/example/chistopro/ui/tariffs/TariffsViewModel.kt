package com.example.chistopro.ui.tariffs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chistopro.data.model.Tariff
import com.example.chistopro.data.repository.TariffsRepository
import com.example.chistopro.util.formatArea
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TariffsUiState(
    val isLoading: Boolean = true,
    val tariffs: List<Tariff> = emptyList(),
    val errorMessage: String? = null,
    /** Включён режим редактирования (кнопка «Редактировать тарифы»). */
    val isEditMode: Boolean = false,
    /** Тариф, который сейчас редактируется в окне (null — окно закрыто). */
    val editing: Tariff? = null,
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val isSaving: Boolean = false,
    val dialogError: String? = null
)

@HiltViewModel
class TariffsViewModel @Inject constructor(
    private val tariffsRepository: TariffsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TariffsUiState())
    val uiState: StateFlow<TariffsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() = fetch(showLoading = true)

    /** Тихое обновление при возврате на экран (не мешает, пока открыто окно редактирования). */
    fun refresh() {
        val state = _uiState.value
        if (!state.isLoading && state.editing == null) fetch(showLoading = false)
    }

    private fun fetch(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            tariffsRepository.getTariffs()
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = null, tariffs = list) }
                }
                .onFailure { e ->
                    if (showLoading) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = e.message ?: "Не удалось загрузить тарифы")
                        }
                    }
                }
        }
    }

    fun onEditModeToggle() = _uiState.update { it.copy(isEditMode = !it.isEditMode) }

    /** Нажатие на тариф: в режиме редактирования открывает окно с его данными. */
    fun onTariffClick(tariff: Tariff) {
        if (!_uiState.value.isEditMode) return
        _uiState.update {
            it.copy(
                editing = tariff,
                name = tariff.name,
                description = tariff.description,
                price = formatArea(tariff.pricePerM2),
                dialogError = null
            )
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, dialogError = null) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value, dialogError = null) }

    /** В цене разрешены только цифры и разделитель. */
    fun onPriceChange(value: String) = _uiState.update {
        it.copy(price = value.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }, dialogError = null)
    }

    fun onDialogDismiss() {
        if (!_uiState.value.isSaving) _uiState.update { it.copy(editing = null, dialogError = null) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        val editing = state.editing ?: return
        if (state.isSaving) return

        val name = state.name.trim()
        val price = state.price.replace(',', '.').toDoubleOrNull()

        val error = when {
            name.isBlank() -> "Введите название тарифа"
            price == null || price < 0 || price > MAX_PRICE -> "Введите корректную стоимость за 1 м²"
            else -> null
        }
        if (error != null || price == null) {
            _uiState.update { it.copy(dialogError = error) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, dialogError = null) }
            tariffsRepository.updateTariff(editing.id, name, state.description.trim(), price)
                .onSuccess { updated ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            editing = null,
                            tariffs = it.tariffs.map { t -> if (t.id == updated.id) updated else t }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, dialogError = e.message ?: "Не удалось сохранить тариф")
                    }
                }
        }
    }

    private companion object {
        const val MAX_PRICE = 1_000_000.0
    }
}