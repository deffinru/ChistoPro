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

Вход: логин `admin`, пароль `admin` (заглушка FakeAuthRepository).
