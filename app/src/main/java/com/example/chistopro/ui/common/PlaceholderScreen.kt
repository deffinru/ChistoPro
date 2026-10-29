package com.example.chistopro.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chistopro.ui.theme.BackgroundDark
import com.example.chistopro.ui.theme.TextSecondary

/** Заглушка для страниц, которые ещё не сделаны. Заменяется настоящим экраном в AppNavigation. */
@Composable
fun PlaceholderScreen(
    title: String,
    onBack: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, color = Color.White, fontSize = 22.sp)
            Text("Страница в разработке", color = TextSecondary, fontSize = 14.sp)
        }
    }
}
