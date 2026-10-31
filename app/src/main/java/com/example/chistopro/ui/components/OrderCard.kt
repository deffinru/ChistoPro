package com.example.chistopro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.chistopro.ui.theme.CardBorder
import com.example.chistopro.ui.theme.SurfaceCard
import com.example.chistopro.ui.theme.TextSecondary
import com.example.chistopro.util.formatArea
import com.example.chistopro.util.formatPrice
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun Order.chipText(): String =
    if (status == OrderStatus.NEW) scheduledAt?.format(timeFormatter) ?: status.label() // как на макете: время начала
    else status.label()

/**
 * Карточка заказа.
 * showDetails = false — компактная (главная), true — с площадью и стоимостью.
 * onClick = null — карточка не нажимается (на странице самого заказа).
 */
@Composable
fun OrderCard(
    order: Order,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    showDetails: Boolean = false
) {
    val shape = RoundedCornerShape(20.dp)
    val accent = order.status.accentColor()
    val isFinished = !order.status.isActive

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
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
                        .background(accent)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = order.address,
                    color = if (isFinished) TextSecondary else Color.White,
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
                    .background(order.status.chipBackground())
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = order.chipText(),
                    color = accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (showDetails) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "${formatArea(order.area)} м²",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Примерная стоимость", color = TextSecondary, fontSize = 14.sp)
                Text(
                    text = formatPrice(order.totalPrice),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (!isFinished) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${order.serviceName}, ${formatArea(order.area)} м²",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}
