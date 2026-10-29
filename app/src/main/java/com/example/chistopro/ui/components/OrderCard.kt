package com.example.chistopro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chistopro.data.model.Order
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.ui.theme.AccentCyan
import com.example.chistopro.ui.theme.AccentGreen
import com.example.chistopro.ui.theme.AccentOrange
import com.example.chistopro.ui.theme.CardBorder
import com.example.chistopro.ui.theme.ChipCyanBg
import com.example.chistopro.ui.theme.ChipGreenBg
import com.example.chistopro.ui.theme.ChipOrangeBg
import com.example.chistopro.ui.theme.SurfaceCard
import com.example.chistopro.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private data class StatusStyle(val accent: Color, val chipBackground: Color)

private fun OrderStatus.style() = when (this) {
    OrderStatus.IN_PROGRESS -> StatusStyle(AccentCyan, ChipCyanBg)
    OrderStatus.NEW -> StatusStyle(AccentOrange, ChipOrangeBg)
    OrderStatus.DONE -> StatusStyle(AccentGreen, ChipGreenBg)
}

/** Карточка заказа. Используется на главной и позже на странице «Заказы». */
@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val style = order.status.style()
    val isDone = order.status == OrderStatus.DONE

    val chipText = when (order.status) {
        OrderStatus.IN_PROGRESS -> "В работе"
        OrderStatus.NEW -> order.scheduledAt.format(timeFormatter)
        OrderStatus.DONE -> "Готово"
    }
    val subtitle: String? = when (order.status) {
        OrderStatus.IN_PROGRESS -> {
            val start = order.scheduledAt.format(timeFormatter)
            val end = order.scheduledAt.plusHours(order.durationHours.toLong()).format(timeFormatter)
            "$start – $end"
        }
        OrderStatus.NEW -> "${order.serviceName}, ${order.durationHours} ч."
        OrderStatus.DONE -> null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(style.accent)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = order.address,
                    color = if (isDone) TextSecondary else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(style.chipBackground)
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = chipText,
                    color = style.accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}
