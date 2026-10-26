# ЧистоPro — приложение управляющего

Kotlin · Jetpack Compose · MVVM · Hilt

## Структура (MVVM)

    com.example.chistopro
    ├── ChistoProApp.kt            Application (включает Hilt)
    ├── MainActivity.kt            единственная Activity
    ├── data/                      MODEL: данные
    │   └── repository/            репозитории (интерфейс + реализация)
    │   (позже: remote/ — Retrofit, dto/ — модели ответов API)
    ├── di/                        Hilt-модули (кто кого создаёт)
    └── ui/                        VIEW + VIEWMODEL
        ├── navigation/            маршруты и NavHost
        ├── theme/                 цвета, тема, шрифты
        ├── login/                 LoginScreen (View) + LoginViewModel
        └── main/                  главная панель
        (позже: orders/, workers/, services/, reports/)

## Как добавить новый экран (например, Заказы)
1. Создать пакет `ui/orders`.
2. `OrdersViewModel.kt` — состояние (UiState) и логика, данные берёт из репозитория.
3. `OrdersScreen.kt` — только отрисовка по UiState.
4. `data/repository/OrdersRepository.kt` (+ реализация) и привязка в `di/AppModule.kt`.
5. Маршрут в `ui/navigation/AppNavigation.kt`.

Вход: логин `admin`, пароль `admin` (заглушка FakeAuthRepository).
