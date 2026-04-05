# Инструкция по сборке APK-файла через командную строку

## Требования

1. **Java Development Kit (JDK)** версии 11 или выше
2. **Android SDK** (установлен через Android Studio или отдельно)
3. **Gradle** (включен в проект через Gradle Wrapper)

## Шаги по сборке

### 1. Открыть командную строку (Terminal/PowerShell)

Перейдите в корневую директорию проекта:
```bash
cd D:\AndroidProjects\PR2
```

### 2. Проверка наличия Gradle Wrapper

Убедитесь, что в проекте есть файлы:
- `gradlew` (для Linux/Mac)
- `gradlew.bat` (для Windows)

### 3. Сборка Debug APK

Для сборки debug версии APK выполните команду:

**Windows:**
```bash
.\gradlew.bat assembleDebug
```

**Linux/Mac:**
```bash
./gradlew assembleDebug
```

### 4. Сборка Release APK

Для сборки release версии APK выполните команду:

**Windows:**
```bash
.\gradlew.bat assembleRelease
```

**Linux/Mac:**
```bash
./gradlew assembleRelease
```

**Примечание:** Для release сборки может потребоваться настройка подписи приложения в файле `app/build.gradle.kts`.

### 5. Расположение собранного APK

После успешной сборки APK файл будет находиться по пути:

**Debug APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
app/build/outputs/apk/release/app-release.apk
```

## Дополнительные команды

### Очистка проекта перед сборкой

**Windows:**
```bash
.\gradlew.bat clean
```

**Linux/Mac:**
```bash
./gradlew clean
```

### Сборка и установка на подключенное устройство

**Windows:**
```bash
.\gradlew.bat installDebug
```

**Linux/Mac:**
```bash
./gradlew installDebug
```

### Просмотр всех доступных задач Gradle

**Windows:**
```bash
.\gradlew.bat tasks
```

**Linux/Mac:**
```bash
./gradlew tasks
```

## Устранение проблем

### Ошибка: "gradlew не является внутренней или внешней командой"

Убедитесь, что вы находитесь в корневой директории проекта и используете правильный файл:
- Windows: `gradlew.bat`
- Linux/Mac: `gradlew`

### Ошибка: "SDK location not found"

Убедитесь, что файл `local.properties` содержит правильный путь к Android SDK:
```
sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
```

### Ошибка компиляции

1. Убедитесь, что все зависимости загружены:
   ```bash
   .\gradlew.bat build --refresh-dependencies
   ```

2. Очистите проект и пересоберите:
   ```bash
   .\gradlew.bat clean build
   ```

## Пример полной последовательности команд

```bash
# Переход в директорию проекта
cd D:\AndroidProjects\PR2

# Очистка предыдущих сборок
.\gradlew.bat clean

# Сборка debug APK
.\gradlew.bat assembleDebug

# APK будет находиться в app/build/outputs/apk/debug/app-debug.apk
```

