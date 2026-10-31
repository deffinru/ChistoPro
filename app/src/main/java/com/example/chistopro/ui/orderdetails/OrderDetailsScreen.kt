package com.example.chistopro.ui.orderdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chistopro.data.model.Employee
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.ui.components.AllStatuses
import com.example.chistopro.ui.components.OrderCard
import com.example.chistopro.ui.components.ScreenHeader
import com.example.chistopro.ui.components.accentColor
import com.example.chistopro.ui.components.chipBackground
import com.example.chistopro.ui.components.label
import com.example.chistopro.ui.theme.*
import com.example.chistopro.util.formatDateTime
import com.example.chistopro.util.formatFullDate
import com.example.chistopro.util.formatPrice

@Composable
fun OrderDetailsScreen(
    onBack: () -> Unit,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val order = state.order

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

            order == null -> {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = state.errorMessage ?: "Заказ не найден",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = viewModel::load,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                    ) { Text("Повторить") }
                }
            }

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ScreenHeader(
                    subtitle = "ЧИСТО PRO — Заказы",
                    title = "Заказ #${order.id}",
                    dateText = order.scheduledAt?.let { formatFullDate(it) } ?: "Дата не указана",
                    onBack = onBack
                )

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OrderCard(order = order, onClick = null, showDetails = true)

                    InfoCard(order)

                    StatusCard(
                        current = order.status,
                        isSaving = state.isSaving,
                        error = state.actionError,
                        onStatusClick = viewModel::onStatusClick
                    )

                    EmployeeCard(
                        order = order,
                        isSaving = state.isSaving,
                        onChangeClick = viewModel::onEmployeeCardClick
                    )
                }
            }
        }
    }

    if (state.showEmployeeDialog) {
        EmployeeDialog(
            employees = state.employees,
            selectedId = order?.employeeId,
            onSelect = viewModel::onEmployeeSelected,
            onDismiss = viewModel::onEmployeeDialogDismiss
        )
    }

    if (state.showCancelDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onCancelDismiss,
            containerColor = SurfaceCard,
            titleContentColor = Color.White,
            textContentColor = TextSecondary,
            title = { Text("Отменить заказ?") },
            text = { Text("Статус заказа изменится на «Отменён». Его можно будет вернуть, выбрав другой статус.") },
            confirmButton = {
                TextButton(onClick = viewModel::onCancelConfirm) { Text("Отменить заказ", color = AccentRed) }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onCancelDismiss) { Text("Назад", color = AccentCyan) }
            }
        )
    }
}

// ---------- карточки ----------

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .padding(16.dp)
    ) {
        Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = Color.White,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 14.sp)
        Spacer(Modifier.width(16.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

/** Клиент, телефон, тариф, тип помещения, время, комментарий. */
@Composable
private fun InfoCard(order: Order) {
    val context = LocalContext.current
    SectionCard(title = "Информация о заказе") {
        InfoRow("Клиент", order.clientName.ifBlank { "—" })

        if (order.clientPhone.isBlank()) {
            InfoRow("Телефон", "—")
        } else {
            // нажатие открывает набор номера
            InfoRow(
                label = "Телефон",
                value = order.clientPhone,
                valueColor = AccentCyan,
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(order.clientPhone)))
                    context.startActivity(intent)
                }
            )
        }

        InfoRow("Тариф", "${order.serviceName} · ${formatPrice(order.pricePerM2)}/м²")
        if (!order.roomType.isNullOrBlank()) InfoRow("Тип помещения", order.roomType)
        InfoRow("Дата и время", order.scheduledAt?.let { formatDateTime(it) } ?: "—")
        if (!order.comment.isNullOrBlank()) InfoRow("Комментарий", order.comment)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatusCard(
    current: OrderStatus,
    isSaving: Boolean,
    error: String?,
    onStatusClick: (OrderStatus) -> Unit
) {
    SectionCard(title = "Изменить статус заказа") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AllStatuses.forEach { status ->
                val selected = status == current
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (selected) status.chipBackground() else ChipNeutralBg)
                        .clickable(enabled = !isSaving) { onStatusClick(status) }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = status.label(),
                        color = if (selected) status.accentColor() else TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        if (isSaving) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AccentCyan,
                trackColor = ChipNeutralBg
            )
        } else if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        } else {
            Text(
                text = "Выберите новый статус, чтобы обновить карточку заказа.",
                color = TextMuted,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun EmployeeCard(
    order: Order,
    isSaving: Boolean,
    onChangeClick: () -> Unit
) {
    SectionCard(title = "Сотрудник") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = order.employeeName ?: "Не назначен",
                color = if (order.employeeName != null) Color.White else TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = onChangeClick,
                enabled = !isSaving,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonMuted,
                    contentColor = Color.White,
                    disabledContainerColor = ButtonMuted.copy(alpha = 0.6f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                )
            ) {
                Text(if (order.employeeName == null) "Назначить" else "Изменить", fontSize = 14.sp)
            }
        }
    }
}

/** Выбор сотрудника из списка (или «Не назначен»). */
@Composable
private fun EmployeeDialog(
    employees: List<Employee>,
    selectedId: Int?,
    onSelect: (Employee?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        titleContentColor = Color.White,
        title = { Text("Выберите сотрудника") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (employees.isEmpty()) {
                    Text(
                        "Сотрудников пока нет. Их нужно добавить в базу данных (роль employee).",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
                EmployeeOption(
                    title = "Не назначен",
                    subtitle = null,
                    selected = selectedId == null,
                    onClick = { onSelect(null) }
                )
                employees.forEach { employee ->
                    EmployeeOption(
                        title = employee.name,
                        subtitle = employee.phone.ifBlank { null },
                        selected = employee.id == selectedId,
                        onClick = { onSelect(employee) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть", color = AccentCyan) }
        }
    )
}

@Composable
private fun EmployeeOption(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) ChipCyanBg else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (selected) AccentCyan else Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(subtitle, color = TextSecondary, fontSize = 13.sp)
            }
        }
        if (selected) Text("✓", color = AccentCyan, fontSize = 16.sp)
    }
}
