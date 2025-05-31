# Используем официальный образ с Java 17
FROM eclipse-temurin:17-jdk-alpine

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем собранный jar из target в контейнер
COPY target/taskmanager-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду запуска приложения
ENTRYPOINT ["java","-jar","app.jar"]

# Открываем порт 8080
EXPOSE 8080