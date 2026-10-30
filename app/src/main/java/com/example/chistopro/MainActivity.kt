package com.example.chistopro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chistopro.data.local.TokenStore
import com.example.chistopro.ui.navigation.AppNavigation
import com.example.chistopro.ui.navigation.Routes
import com.example.chistopro.ui.theme.ChistoProTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenStore: TokenStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Если токен сохранён — сразу открываем главную, иначе экран входа
        val startDestination = if (tokenStore.getToken() != null) Routes.HOME else Routes.LOGIN

        setContent {
            ChistoProTheme {
                AppNavigation(
                    startDestination = startDestination,
                    sessionExpired = tokenStore.sessionExpired
                )
            }
        }
    }
}
