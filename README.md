# Medical Platform

## О проекте
Микросервисная платформа для автоматизации работы сети медицинских центров.

## Микросервисы
- **schedule-service** (8081) — расписание врачей
- **patient-service** (8082) — карточки пациентов
- **notification-service** (8083) — уведомления
- **integration-service** (8084) — интеграция с лабораториями

## Технологии
- Java 17 + Spring Boot 3.5
- PostgreSQL + JPA/Hibernate
- Apache Kafka
- Redis (кэширование)
- Docker + Docker Compose
- Swagger UI
- Lombok + Bean Validation
- Transactional Outbox pattern

## Запуск
1. `docker-compose up -d`
2. Запустить каждый сервис
3. Swagger: `http://localhost:{port}/swagger-ui/index.html`
