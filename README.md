# SnakeReflection — Курсовой проект

Игра "Змейка" с отражением от стен и графическим интерфейсом 

}Основные возможности

- Змейка отражается от стен (угол падения = угол отражения)
- Изменение скорости клавишами `+` и `-`
- Сбор еды (красные шарики) и увеличение длины змейки
- Отображение текущего счёта и скорости
- Логирование в консоль и файл с использованием Log4j 2
- Unit-тесты с использованием JUnit 5
- Сборка и запуск через Gradle

}Структура проекта

SnakeReflection---Kyrsach/
-build.gradle
-settings.gradle
-gradlew
-gradlew.bat
-.gitignore
-README.md
-src/
--main/java/… # Исходный код игры
-main/resources/… # Конфиги и ресурсы
--test/java/… # Unit-тесты

}Сборка и запуск

1. **Сборка JAR:**

```bash
gradlew clean jar

2. **Запуск JAR:**
java -jar build/libs/SnakeReflection.jar

}Тестирование

Запуск unit-тестов через Gradle:

gradlew test

Результаты тестов будут в папке build/reports/tests/test/index.html.

}Ссылка на репозиторий

GitHub: SnakeReflection

Автор: IzaoS
Версия: 1.0
