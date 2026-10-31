package com.example.chistopro.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

/** Сегодняшняя дата для шапки. Обновляется при возврате на экран и в полночь. */
@Composable
fun rememberTodayText(): String {
    var text by remember { mutableStateOf(formatFullDate(LocalDateTime.now())) }

    LifecycleResumeEffect(Unit) {
        text = formatFullDate(LocalDateTime.now())
        onPauseOrDispose { }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalDateTime.now()
            val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
            delay(Duration.between(now, midnight).toMillis() + 1_000)
            text = formatFullDate(LocalDateTime.now())
        }
    }
    return text
}