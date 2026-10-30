package com.example.chistopro.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chistopro.ui.common.PlaceholderScreen
import com.example.chistopro.ui.home.HomeScreen
import com.example.chistopro.ui.login.LoginScreen
import com.example.chistopro.ui.orders.OrdersScreen
import com.example.chistopro.ui.profile.EditProfileScreen
import com.example.chistopro.ui.profile.ProfileScreen
import com.example.chistopro.ui.theme.BackgroundDark
import kotlinx.coroutines.flow.Flow

/** Адреса экранов. Новый экран = новая константа + composable в NavHost. */
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val ORDERS = "orders"
    const val ANALYTICS = "analytics"
    const val TARIFFS = "tariffs"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile/edit"

    const val ORDER_ID_ARG = "orderId"
    const val ORDER_DETAILS = "order/{$ORDER_ID_ARG}"
    fun orderDetails(orderId: Int) = "order/$orderId"
}

@Composable
fun AppNavigation(
    startDestination: String,
    sessionExpired: Flow<Unit>
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Токен перестал действовать -> возвращаем на экран входа
    LaunchedEffect(Unit) {
        sessionExpired.collect {
            navController.navigate(Routes.LOGIN) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    // Нижняя панель видна только на четырёх основных страницах
    val showBottomBar = BottomTab.entries.any { it.route == currentRoute }

    // Переход между вкладками без накопления экранов в стеке
    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(Routes.HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // отступы от системных панелей делает каждый экран сам
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(currentRoute = currentRoute, onTabClick = { navigateToTab(it) })
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            // убираем экран входа из стека, чтобы "Назад" не возвращал на него
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onOrderClick = { id -> navController.navigate(Routes.orderDetails(id)) },
                    onProfileClick = { navController.navigate(Routes.PROFILE) },
                    onAllOrdersClick = { navigateToTab(Routes.ORDERS) }
                )
            }

            // Страницы-заглушки (аналитика, тарифы, заказ): заменяй PlaceholderScreen на настоящий экран
            composable(Routes.ORDERS) {
                OrdersScreen(onOrderClick = { id -> navController.navigate(Routes.orderDetails(id)) })
            }
            composable(Routes.ANALYTICS) { PlaceholderScreen(title = "Аналитика") }
            composable(Routes.TARIFFS) { PlaceholderScreen(title = "Тарифы") }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onEditClick = { navController.navigate(Routes.PROFILE_EDIT) },
                    onLoggedOut = {
                        // после выхода очищаем весь стек и открываем экран входа
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.PROFILE_EDIT) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Routes.ORDER_DETAILS,
                arguments = listOf(navArgument(Routes.ORDER_ID_ARG) { type = NavType.IntType })
            ) { entry ->
                val orderId = entry.arguments?.getInt(Routes.ORDER_ID_ARG) ?: 0
                PlaceholderScreen(
                    title = "Заказ №$orderId",
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
