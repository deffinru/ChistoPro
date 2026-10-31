package com.example.chistopro.ui.tariffs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chistopro.data.model.Tariff
import com.example.chistopro.ui.components.AppTextField
import com.example.chistopro.ui.components.ScreenHeader
import com.example.chistopro.ui.theme.*
import com.example.chistopro.util.formatArea

@Composable
fun TariffsScreen(
    viewModel: TariffsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AccentCyan
            )

            state.errorMessage != null -> Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = state.errorMessage ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = viewModel::load,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                ) { Text("Повторить") }
            }

            else -> Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        ScreenHeader(
                            subtitle = "ЧИСТО PRO — Управление",
                            title = "Тарифы на уборку",
                            dateText = "Стоимость за 1 м²"
                        )
                    }
                    item {
                        Text(
                            text = "Виды уборки",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 10.dp)
                        )
                    }
                    if (state.tariffs.isEmpty()) {
                        item {
                            Text(
                                text = "Тарифов пока нет",
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            )
                        }
                    } else {
                        items(state.tariffs, key = { it.id }) { tariff ->
                            TariffCard(
                                tariff = tariff,
                                editMode = state.isEditMode,
                                onClick = { viewModel.onTariffClick(tariff) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }

                Button(
                    onClick = viewModel::onEditModeToggle,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentCyan,
                        contentColor = BackgroundDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .height(56.dp)
                ) {
                    Text(
                        text = if (state.isEditMode) "Готово" else "Редактировать тарифы",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (state.editing != null) {
        EditTariffDialog(
            name = state.name,
            description = state.description,
            price = state.price,
            isSaving = state.isSaving,
            error = state.dialogError,
            onNameChange = viewModel::onNameChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onPriceChange = viewModel::onPriceChange,
            onSave = viewModel::onSaveClick,
            onDismiss = viewModel::onDialogDismiss
        )
    }
}

@Composable
private fun TariffCard(
    tariff: Tariff,
    editMode: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, if (editMode) AccentCyan.copy(alpha = 0.6f) else CardBorder, shape)
            .then(if (editMode) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tariff.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (tariff.description.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(text = tariff.description, color = TextSecondary, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.width(12.dp))
        if (editMode) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Изменить",
                tint = AccentCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatArea(tariff.pricePerM2),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "₽/м²", color = TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun EditTariffDialog(
    name: String,
    description: String,
    price: String,
    isSaving: Boolean,
    error: String?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        titleContentColor = Color.White,
        title = { Text("Редактирование тарифа") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = "Название",
                    enabled = !isSaving
                )
                AppTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = "Описание",
                    enabled = !isSaving
                )
                AppTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = "Стоимость за 1 м², ₽",
                    keyboardType = KeyboardType.Decimal,
                    isLast = true,
                    enabled = !isSaving,
                    onDone = onSave
                )
                if (error != null) {
                    Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave, enabled = !isSaving) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = AccentCyan
                    )
                } else {
                    Text("Сохранить", color = AccentCyan)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Отмена", color = TextSecondary)
            }
        }
    )
}