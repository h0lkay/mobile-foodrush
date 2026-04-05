# FoodRush - Приложение для доставки еды

## Описание проекта

FoodRush - это Android приложение для заказа еды с доставкой. Приложение реализует пользовательский интерфейс, спроектированный в практической работе 1, с использованием статических данных.

## Структура приложения

Приложение состоит из следующих экранов:

1. **SplashActivity** - Экран загрузки с логотипом приложения
2. **MainActivity** - Главный экран с выбором ресторана
3. **RestaurantActivity** - Детальная информация о ресторане и меню
4. **FoodActivity** - Детальная информация о блюде
5. **AddressActivity** - Форма ввода адреса доставки

## Технологии

- **Kotlin** - язык программирования
- **Android SDK** - платформа разработки
- **Material Design** - компоненты UI
- **CardView** - для отображения карточек
- **TextInputLayout** - для полей ввода

## Требования

- Android Studio или другая IDE для Android разработки
- Android SDK (минимальная версия: API 24, целевая версия: API 36)
- JDK 11 или выше
- Gradle (включен в проект через Gradle Wrapper)

## Установка и запуск

### Через Android Studio

1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle
3. Подключите Android устройство или запустите эмулятор
4. Нажмите кнопку "Run" или используйте сочетание клавиш `Shift + F10`

### Через командную строку

См. файл [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) для подробных инструкций по сборке APK через командную строку.

## Структура проекта

```
PR2/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/kolobanovda_ki23_16_1b_pr2/
│   │       │   ├── SplashActivity.kt
│   │       │   ├── MainActivity.kt
│   │       │   ├── RestaurantActivity.kt
│   │       │   ├── FoodActivity.kt
│   │       │   └── AddressActivity.kt
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_splash.xml
│   │       │   │   ├── activity_main.xml
│   │       │   │   ├── activity_restaurant.xml
│   │       │   │   ├── activity_food.xml
│   │       │   │   └── activity_address.xml
│   │       │   └── values/
│   │       │       └── strings.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── BUILD_INSTRUCTIONS.md
└── README.md
```

## Особенности реализации

- Все данные статические (захардкожены в XML layouts)
- Использование Material Design компонентов
- Адаптивный дизайн с использованием ScrollView
- Навигация между экранами через Intent

## Сборка APK

### Debug версия

```bash
.\gradlew.bat assembleDebug
```

APK будет находиться в: `app/build/outputs/apk/debug/app-debug.apk`

### Release версия

```bash
.\gradlew.bat assembleRelease
```

APK будет находиться в: `app/build/outputs/apk/release/app-release.apk`

Подробные инструкции см. в [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)

## Автор

Колобанов Д.А. (KI23-16-1B)

## Лицензия

Этот проект создан в учебных целях.

