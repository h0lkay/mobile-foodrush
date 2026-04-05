# Mobile Food Rush

## Сборка проекта

### Требования
- Android Studio
- JDK 11 или выше
- Android SDK
- Gradle 7.0+

### Инструкция по сборке

#### 1. Клонирование репозитория

```bash
git clone https://github.com/h0lkay/mobile-foodrush.git
cd mobile-foodrush
```

#### 2. Сборка APK

Через Android Studio:

Build → Build Bundle(s) / APK(s) → Build APK(s)

Готовый APK будет в app/build/outputs/apk/debug/

Через командную строку:

```bash
./gradlew assembleDebug
```
