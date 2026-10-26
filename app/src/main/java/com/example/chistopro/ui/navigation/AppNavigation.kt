package com.example.chistopro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chistopro.ui.login.LoginScreen
import com.example.chistopro.ui.main.MainPlaceholderScreen

/** Адреса экранов. Новый экран = новая константа + composable в NavHost. */
object Routes {
    const val LOGIN = "login"
    const val MAIN = "main"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN) {
                        // убираем экран входа из стека, чтобы "Назад" не возвращал на него
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.MAIN) { MainPlaceholderScreen() }
    }
}
