# Реализация JWT-аутентификации в Spring Boot

Данный проект демонстрирует реализацию аутентификации с использованием JWT (JSON Web Token) в приложении на основе Spring Boot. Программа генерирует два типа токенов: **Access Token** и **Refresh Token**, обеспечивая безопасный и удобный механизм работы с сессиями пользователей.

## Описание

JWT-аутентификация предоставляет безопасный способ передачи данных между клиентом и сервером. В этом проекте реализована генерация и валидация двух видов токенов:

- **Access Token** — используется для доступа к защищенным ресурсам API. Имеет короткий срок действия, что минимизирует риски утечки данных.
- **Refresh Token** — используется для получения нового Access Token после истечения его срока действия. Имеет более продолжительный срок жизни и хранится в защищенных местах, таких как HTTP-only cookies.

### Основные функции:
- Генерация и валидация JWT-токенов (access и refresh)
- Обновление Access Token с помощью Refresh Token
- Настройка сроков действия токенов
- Защита API с использованием JWT

## Стек технологий

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (jjwt)**

## Установка и запуск

1. **Клонировать репозиторий:**

    ```bash
    git clone https://github.com/nurfet78/jwt-auth-spring-boot.git
    ```

2. **Перейти в папку проекта:**

    ```bash
    cd jwt-auth-spring-boot
    ```

3. **Настроить конфигурацию JWT:**

   В файле `application.properties` или `application.yml` необходимо задать секретные ключи для access и refresh токенов, а также их сроки действия:

    ```properties
    jwt.access.secret=yourAccessSecretKey
    jwt.refresh.secret=yourRefreshSecretKey
    jwt.access.expiration=15 # в минутах
    jwt.refresh.expiration=1440 # в минутах (1 день)
    ```

4. **Собрать и запустить приложение:**

    ```bash
    ./mvnw spring-boot:run
    ```

5. **Использование API:**

    - Для генерации токенов отправьте POST-запрос с данными пользователя на `/api/auth/signin`.
    - Для обновления Access Token используйте Refresh Token на эндпоинте `/api/auth/token`.

## Эндпоинты API

- **POST** `/api/auth/signin` — аутентификация пользователя, возвращает Access и Refresh токены.
- **POST** `/api/auth/token` — обновление Access Token с использованием Refresh Token.
- **GET** `/api/users` — защищенный ресурс, доступный только при наличии валидного Access Token.

## Пример запросов

### 1. Аутентификация пользователя (генерация токенов):

```bash
POST /api/auth/signin
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
