package com.example.chistopro.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chistopro.ui.theme.AccentCyan
import com.example.chistopro.ui.theme.CardBorder
import com.example.chistopro.ui.theme.ChipCyanBg
import com.example.chistopro.ui.theme.NavBarBackground
import com.example.chistopro.ui.theme.TextMuted

/** Вкладки нижней панели: Главная, Заказы, Аналитика, Тарифы. */
enum class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    HOME(Routes.HOME, "Главная", Icons.Outlined.Home),
    ORDERS(Routes.ORDERS, "Заказы", Icons.AutoMirrored.Outlined.Assignment),
    ANALYTICS(Routes.ANALYTICS, "Аналитика", Icons.Outlined.BarChart),
    TARIFFS(Routes.TARIFFS, "Тарифы", Icons.Outlined.LocalOffer)
}

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onTabClick: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(NavBarBackground)
    ) {
        HorizontalDivider(color = CardBorder)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp)
        ) {
            BottomTab.entries.forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    selected = tab.route == currentRoute,
                    onClick = { onTabClick(tab.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: BottomTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) AccentCyan else TextMuted
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (selected) ChipCyanBg else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = tab.label,
            color = color,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
