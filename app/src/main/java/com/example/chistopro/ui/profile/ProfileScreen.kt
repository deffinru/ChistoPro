package com.example.chistopro.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chistopro.ui.components.headerBackground
import com.example.chistopro.ui.theme.*

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) onLoggedOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Шапка: кнопка «Назад», под ней аватар слева, подпись и имя справа
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .headerBackground()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.padding(start = 4.dp, top = 4.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(ChipNeutralBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.avatarLetter,
                        color = AccentCyan,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(24.dp))
                Column {
                    Text("ЧИСТО PRO — Профиль", color = AccentCyan, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.weight(3f))

        ProfileButton(text = "Редактировать профиль", onClick = onEditClick)
        Spacer(Modifier.height(14.dp))
        ProfileButton(text = "Выйти из профиля", onClick = viewModel::onLogoutClick)

        Spacer(Modifier.weight(1f))
    }

    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onLogoutDismiss,
            containerColor = SurfaceCard,
            titleContentColor = Color.White,
            textContentColor = TextSecondary,
            title = { Text("Выйти из профиля?") },
            text = { Text("Чтобы продолжить работу, нужно будет снова войти.") },
            confirmButton = {
                TextButton(onClick = viewModel::onLogoutConfirm) {
                    Text("Выйти", color = AccentRed)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onLogoutDismiss) {
                    Text("Отмена", color = AccentCyan)
                }
            }
        )
    }
}

@Composable
private fun ProfileButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonMuted,
            contentColor = Color.White
        ),
        modifier = Modifier
            .width(240.dp)
            .height(50.dp)
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}
