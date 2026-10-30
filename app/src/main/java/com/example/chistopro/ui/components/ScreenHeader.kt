package com.example.chistopro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chistopro.ui.theme.AccentCyan
import com.example.chistopro.ui.theme.ChipNeutralBg
import com.example.chistopro.ui.theme.HeaderEnd
import com.example.chistopro.ui.theme.HeaderStart

/** Градиентный фон шапки; заходит под статус-бар. */
fun Modifier.headerBackground(): Modifier =
    this
        .background(Brush.horizontalGradient(listOf(HeaderStart, HeaderEnd)))
        .statusBarsPadding()

/** Плашка с датой. */
@Composable
fun DateChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(ChipNeutralBg)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = Color(0xFFB8C7D6), fontSize = 14.sp)
    }
}

/** Общая шапка главной и страницы «Заказы». */
@Composable
fun ScreenHeader(
    subtitle: String,
    title: String,
    modifier: Modifier = Modifier,
    dateText: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .headerBackground()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = subtitle, color = AccentCyan, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            if (dateText != null) {
                Spacer(Modifier.height(12.dp))
                DateChip(dateText)
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(12.dp))
            trailing()
        }
    }
}
