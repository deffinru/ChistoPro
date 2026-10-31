package com.example.chistopro.ui.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chistopro.data.model.OrderStatus
import com.example.chistopro.data.model.WeeklyStats
import com.example.chistopro.ui.components.ScreenHeader
import com.example.chistopro.ui.components.accentColor
import com.example.chistopro.ui.components.label
import com.example.chistopro.ui.theme.*
import com.example.chistopro.util.formatArea
import com.example.chistopro.util.formatPrice
import com.example.chistopro.util.formatWeekRange
import com.example.chistopro.util.rememberTodayText

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val stats = state.stats

    // при возврате на экран тихо обновляем данные
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

            stats == null -> Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = state.errorMessage ?: "Нет данных",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = viewModel::load,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                ) { Text("Повторить") }
            }

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ScreenHeader(
                    subtitle = "ЧИСТО PRO — Аналитика",
                    title = "Аналитика заказов",
                    dateText = rememberTodayText()
                )
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OverviewCard(stats)
                    StatusesCard(stats)
                }
            }
        }
    }
}

@Composable
private fun CardContainer(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .padding(16.dp),
        content = content
    )
}

/** Обзор за неделю: заказы, выручка, площадь, средний чек. */
@Composable
private fun OverviewCard(stats: WeeklyStats) {
    CardContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Обзор за неделю", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                text = formatWeekRange(stats.weekStart, stats.weekEnd),
                color = TextMuted,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem("Заказов", stats.ordersCount.toString(), Modifier.weight(1f))
            StatItem("Выручка", formatPrice(stats.revenue), Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem("Площадь", "${formatArea(stats.totalArea)} м²", Modifier.weight(1f))
            StatItem("Средний чек", formatPrice(stats.averageCheck), Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

/** График «Заказы по статусам»: чем больше заказов, тем длиннее полоса. */
@Composable
private fun StatusesCard(stats: WeeklyStats) {
    val maxCount = maxOf(1, stats.statusCounts.values.maxOrNull() ?: 1)

    CardContainer {
        Text("Заказы по статусам", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OrderStatus.entries.forEachIndexed { index, status ->
            if (index > 0) Spacer(Modifier.height(14.dp))
            StatusBarRow(
                label = status.label(),
                count = stats.statusCounts[status] ?: 0,
                maxCount = maxCount,
                color = status.accentColor()
            )
        }
    }
}

@Composable
private fun StatusBarRow(label: String, count: Int, maxCount: Int, color: Color) {
    val progress by animateFloatAsState(
        targetValue = count.toFloat() / maxCount,
        label = "statusBar"
    )
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextSecondary, fontSize = 14.sp)
            Text(count.toString(), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(ChipNeutralBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}