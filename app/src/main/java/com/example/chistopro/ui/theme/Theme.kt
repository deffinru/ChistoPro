package com.example.chistopro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Цвета подобраны по макету. Точные HEX можно взять в Figma (Inspect) и заменить здесь.
val BackgroundDark = Color(0xFF0E1A24)
val FieldBackground = Color(0xFF1B2B3C)
val FieldHint = Color(0xFF7D8B9A)
val ButtonBlue = Color(0xFF1D4E7A)
val ErrorRed = Color(0xFFFF6B6B)

private val ColorsDark = darkColorScheme(
    background = BackgroundDark,
    surface = BackgroundDark,
    primary = ButtonBlue,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorRed
)

@Composable
fun ChistoProTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColorsDark, content = content)
}
