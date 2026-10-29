package com.example.chistopro.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chistopro.R
import com.example.chistopro.ui.theme.BackgroundDark
import com.example.chistopro.ui.theme.ButtonBlue
import com.example.chistopro.ui.theme.ChistoProTheme
import com.example.chistopro.ui.theme.FieldBackground
import com.example.chistopro.ui.theme.FieldHint

/** Stateful-обёртка: связывает экран с ViewModel. */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoginSuccess()
    }

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick
    )
}

/** Stateless-часть: только отрисовка (удобно для Preview). */
@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(16.dp)
    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = FieldBackground,
        unfocusedContainerColor = FieldBackground,
        disabledContainerColor = FieldBackground,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.White.copy(alpha = 0.6f),
        cursorColor = Color.White,
        focusedPlaceholderColor = FieldHint,
        unfocusedPlaceholderColor = FieldHint,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    val fieldTextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_chistopro),
            contentDescription = "Логотип ЧистоPro",
            modifier = Modifier.size(180.dp)
        )

        Spacer(Modifier.height(32.dp))

        TextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = { Text("Введите email", style = fieldTextStyle) },
            textStyle = fieldTextStyle,
            singleLine = true,
            enabled = !state.isLoading,
            shape = fieldShape,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Введите пароль", style = fieldTextStyle) },
            textStyle = fieldTextStyle,
            singleLine = true,
            enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(),
            shape = fieldShape,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onLoginClick()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = !state.isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonBlue,
                contentColor = Color.White,
                disabledContainerColor = ButtonBlue.copy(alpha = 0.6f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(52.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Вход", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginPreview() {
    ChistoProTheme {
        LoginContent(
            state = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}
