#!/bin/bash

# Пересборка Maven-проекта в JAR-файл
echo "Пересобираем Maven-проект в JAR-файл..."
mvn clean package || { echo "Ошибка при сборке проекта. Завершение."; exit 1; }

# Определяем порт для PostgreSQL
POSTGRES_PORT=5432

# Функция для проверки и завершения приложения, занимающего порт
check_and_terminate_app() {
    local PORT=$1
    local SERVICE_NAME=$2

    # Проверяем, занят ли порт
    if lsof -i :$PORT > /dev/null; then
        echo "$SERVICE_NAME порт $PORT занят. Завершаем приложение..."

        # Получаем имя приложения, занимающего порт
        APP_NAME=$(lsof -t -i :$PORT)

        # Завершаем приложение, используя именно его PID
        for PID in $APP_NAME; do
            if [ -n "$PID" ]; then
                kill "$PID" && echo "$SERVICE_NAME приложение с PID $PID завершено." || echo "Не удалось завершить приложение с PID $PID."
            fi
        done
    else
        echo "$SERVICE_NAME порт $PORT свободен."
    fi
}

# Функция для проверки и завершения контейнера Docker, занимающего порт
check_and_terminate_docker_container() {
    local PORT=$1
    local SERVICE_NAME=$2

    # Проверяем, есть ли запущенные контейнеры, использующие указанный порт
    CONTAINER_ID=$(docker ps --filter "publish=$PORT" -q)

    if [ -n "$CONTAINER_ID" ]; then
        echo "$SERVICE_NAME порт $PORT занят контейнером Docker. Завершаем контейнер..."

        # Завершаем контейнер
        docker stop "$CONTAINER_ID" && echo "Контейнер с ID $CONTAINER_ID завершен." || echo "Не удалось завершить контейнер с ID $CONTAINER_ID."
    else
        echo "$SERVICE_NAME порт $PORT свободен для контейнеров Docker."
    fi
}

# Проверяем и завершаем приложения, занимающие порт PostgreSQL
check_and_terminate_app $POSTGRES_PORT "PostgreSQL"

# Проверяем и завершаем контейнеры Docker, занимающие порт PostgreSQL
check_and_terminate_docker_container $POSTGRES_PORT "PostgreSQL"

# Сборка и запуск Docker Compose
echo "Запускаем сборку и запуск Docker Compose..."
docker-compose up --build