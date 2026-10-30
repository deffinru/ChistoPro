package com.example.chistopro.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.chistopro.ui.theme.AccentCyan
import com.example.chistopro.ui.theme.BackgroundDark
import com.example.chistopro.ui.theme.ButtonBlue
import com.example.chistopro.ui.theme.TextSecondary

@Composable
fun OrdersScreen(
    onOrderClick: (Int) -> Unit,
    viewModel: OrdersViewModel = hiltViewModel()
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
                Text(state.errorMessage!!, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center)
                Button(
                    onClick = viewModel::load,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                ) { Text("Повторить") }
            }

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ScreenHeader(
                        subtitle = "ЧИСТО PRO — Заказы",
                        title = "Заказы компании",
                        dateText = state.dateText
                    )
                }
                item {
                    Text(
                        text = "Все заказы",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 10.dp)
                    )
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
                            showDetails = true,
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
