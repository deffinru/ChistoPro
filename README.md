# ЧистоPro — приложение управляющего

Kotlin · Jetpack Compose · MVVM · Hilt

## Структура (MVVM)

    com.example.chistopro
    ├── ChistoProApp.kt            Application (включает Hilt)
    ├── MainActivity.kt            единственная Activity
    ├── data/                      MODEL: данные
    │   ├── model/                 Order, Manager, OrderStatus
    │   └── repository/            репозитории (интерфейс + реализация)
    │   (позже: remote/ — Retrofit, dto/ — модели ответов API)
    ├── di/                        Hilt-модули (кто кого создаёт)
    └── ui/                        VIEW + VIEWMODEL
        ├── navigation/            маршруты и NavHost
        ├── theme/                 цвета, тема, шрифты
        ├── login/                 LoginScreen (View) + LoginViewModel
        ├── home/                  главная страница (HomeScreen + HomeViewModel)
        ├── components/            общие элементы (OrderCard)
        └── common/                PlaceholderScreen
        (позже: orders/, workers/, services/, reports/)

## Как добавить новый экран (например, Заказы)
1. Создать пакет `ui/orders`.
2. `OrdersViewModel.kt` — состояние (UiState) и логика, данные берёт из репозитория.
3. `OrdersScreen.kt` — только отрисовка по UiState.
4. `data/repository/OrdersRepository.kt` (+ реализация) и привязка в `di/AppModule.kt`.
5. Маршрут в `ui/navigation/AppNavigation.kt`.

Вход: email `admin@chistopro.ru`, пароль `admin` (заглушка FakeAuthRepository).

Маршруты: login → home → (profile | orders | analytics | tariffs | order/{id}).
Страницы profile/orders/analytics/tariffs/order пока PlaceholderScreen — заменяются в ui/navigation/AppNavigation.kt.
