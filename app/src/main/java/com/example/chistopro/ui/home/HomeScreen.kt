package com.example.chistopro.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.chistopro.ui.components.OrderCard
import com.example.chistopro.ui.components.ScreenHeader
import com.example.chistopro.ui.theme.*
import com.example.chistopro.util.rememberTodayText

/** Stateful-обёртка: связывает экран с HomeViewModel. */
@Composable
fun HomeScreen(
    onOrderClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onAllOrdersClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // при возврате на экран тихо обновляем данные (например, пришли новые заказы с сайта)
    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    HomeContent(
        state = state,
        onOrderClick = onOrderClick,
        onProfileClick = onProfileClick,
        onAllOrdersClick = onAllOrdersClick,
        onRetry = viewModel::load
    )
}

/** Stateless-часть: только отрисовка. */
@Composable
fun HomeContent(
    state: HomeUiState,
    onOrderClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onAllOrdersClick: () -> Unit,
    onRetry: () -> Unit
) {
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
                    text = state.errorMessage,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                ) { Text("Повторить") }
            }

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ScreenHeader(
                        subtitle = "ЧИСТО PRO — Управление",
                        title = "${state.greeting}, ${state.managerName}",
                        dateText = rememberTodayText(),
                        trailing = {
                            AvatarButton(letter = state.avatarLetter, onClick = onProfileClick)
                        }
                    )
                }
                item {
                    StatCard(
                        count = state.activeCount,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Активные заказы",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Все →",
                            color = AccentCyan,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onAllOrdersClick)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
                if (state.orders.isEmpty()) {
                    item {
                        Text(
                            text = "Заказов пока нет",
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                } else {
                    items(state.orders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

/** Кружок с первой буквой имени — переход в профиль. */
@Composable
private fun AvatarButton(letter: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(ChipCyanBg.copy(alpha = 0.6f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = AccentCyan,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatCard(count: Int, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, CardBorder, shape)
            .padding(20.dp)
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(text = "Активных заказов", color = TextSecondary, fontSize = 13.sp)
    }
}
