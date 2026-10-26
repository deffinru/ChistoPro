package com.example.chistopro.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chistopro.ui.theme.BackgroundDark

/** Заглушка главной панели — заменим на следующем шаге. */
@Composable
fun MainPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Text("Главная панель (скоро)", color = Color.White)
    }
}
