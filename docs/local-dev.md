# Локальная разработка

## Быстрый старт

```powershell
# 1. PostgreSQL
cd C:\work_space\resume
copy .env.example .env
docker compose up database

# 2. Backend (Java 21)
cd resume-website-backend
.\mvnw spring-boot:run

# 3. Website frontend
cd resume-website-frontend
npm install
npm run dev

# 4. Admin frontend
cd resume-admin-frontend
copy .env.example .env
npm install
npm run dev
```

## Порты

| Сервис | URL |
|--------|-----|
| PostgreSQL | `localhost:5400` |
| Backend API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |
| Website | http://localhost:5173 |
| Admin | http://localhost:3000 |

## Credentials

- PostgreSQL: из корневого `.env` (`DATABASE_*`)
- Backend datasource: `application.yaml` → `localhost:5400`
- Admin по умолчанию: `admin` / `admin`

## Демо-проекты (опционально)

После первого запуска backend (Flyway создаст схему) можно залить 10 тестовых проектов — **только для локальной БД**:

```powershell
psql -h localhost -p 5400 -U Resume -d resume -f resume-website-backend/scripts/dev/site_project_demo_seed.sql
```

Скрипт ничего не делает, если в `site_projects` уже есть строки. Чтобы пересоздать сид: очистите таблицы проектов и запустите скрипт снова.

## Telegram (регистрация студента)

См. [telegram-setup.md](./telegram-setup.md). Для локальной проверки нужен публичный HTTPS webhook (ngrok) или staging API.

## Тесты backend

```powershell
cd resume-website-backend
.\mvnw test
```

Интеграционные тесты (Testcontainers) требуют **Docker Desktop**.
