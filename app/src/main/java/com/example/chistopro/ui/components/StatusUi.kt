package com.example.chistopro.ui.components

import androidx.compose.ui.graphics.Color
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.ui.theme.AccentCyan
import com.example.chistopro.ui.theme.AccentGreen
import com.example.chistopro.ui.theme.AccentOrange
import com.example.chistopro.ui.theme.AccentPurple
import com.example.chistopro.ui.theme.AccentRed
import com.example.chistopro.ui.theme.ChipCyanBg
import com.example.chistopro.ui.theme.ChipGreenBg
import com.example.chistopro.ui.theme.ChipOrangeBg
import com.example.chistopro.ui.theme.ChipPurpleBg
import com.example.chistopro.ui.theme.ChipRedBg

/** Название статуса для пользователя. */
fun OrderStatus.label(): String = when (this) {
    OrderStatus.NEW -> "Новый"
    OrderStatus.CONFIRMED -> "Подтверждён"
    OrderStatus.IN_PROGRESS -> "В работе"
    OrderStatus.DONE -> "Готово"
    OrderStatus.CANCELLED -> "Отменён"
}

/** Основной цвет статуса (точка, текст плашки). */
fun OrderStatus.accentColor(): Color = when (this) {
    OrderStatus.NEW -> AccentOrange
    OrderStatus.CONFIRMED -> AccentPurple
    OrderStatus.IN_PROGRESS -> AccentCyan
    OrderStatus.DONE -> AccentGreen
    OrderStatus.CANCELLED -> AccentRed
}

/** Цвет фона плашки статуса. */
fun OrderStatus.chipBackground(): Color = when (this) {
    OrderStatus.NEW -> ChipOrangeBg
    OrderStatus.CONFIRMED -> ChipPurpleBg
    OrderStatus.IN_PROGRESS -> ChipCyanBg
    OrderStatus.DONE -> ChipGreenBg
    OrderStatus.CANCELLED -> ChipRedBg
}

/** Порядок статусов для выбора на странице заказа. */
val AllStatuses: List<OrderStatus> = OrderStatus.entries
