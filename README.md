# ЧистоPro - приложение управляющего

Kotlin · Jetpack Compose · MVVM · Hilt

## Структура (MVVM)

    com.example.chistopro
    ├── ChistoProApp.kt            Application (включает Hilt)
    ├── MainActivity.kt            единственная Activity
    ├── data/                      MODEL: данные
    │   ├── model/                 Order, Manager, OrderStatus
    │   └── repository/            репозитории (интерфейс + реализация)
    │   (позже: remote/ — Retrofit, dto/ — модели ответов API)
    │   ├── remote/                ApiService (Retrofit), DTO, обработка ошибок, AuthInterceptor
    │   └── local/                 TokenStore (токен входа)
    ├── di/                        Hilt-модули (AppModule, NetworkModule)
    └── ui/                        VIEW + VIEWMODEL
        ├── navigation/            маршруты и NavHost
        ├── theme/                 цвета, тема, шрифты
        ├── login/                 LoginScreen (View) + LoginViewModel
        ├── home/                  главная страница (HomeScreen + HomeViewModel)
        ├── orders/                страница «Заказы»
        ├── orderdetails/          страница заказа (статус, сотрудник)
        ├── profile/               профиль + редактирование (имя, пароль)
        ├── components/            общие элементы (OrderCard, ScreenHeader, AppTextField)
        └── common/                PlaceholderScreen
        (позже: orders/, workers/, services/, reports/)

Приложение работает с настоящим API (папка chistopro-api). Вход: manager@chistopro.ru / manager123.
Адрес API задаётся в app/build.gradle.kts (API_BASE_URL): эмулятор - http://10.0.2.2:3000/
Тестовых заглушек больше нет: приложению нужен запущенный API.
