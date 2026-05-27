# Документация resume-web-site-backend

Сводная документация API и домена для разработчиков.

| Документ | Содержание |
|----------|------------|
| [api-endpoints.md](./api-endpoints.md) | **Полный справочник HTTP:** все пути, методы, роли (`@PreAuthorize`), публичные vs auth, исключения по ресурсам |
| [project-passport.md](./project-passport.md) | Полный паспорт: назначение, стек, слои кода, БД/Flyway, безопасность, карта REST, домены, WebSocket, ошибки, NFR, сценарии по ролям |
| [backend.md](./backend.md) | Устройство backend, стек, модель данных, роли, чаты, файлы, публичная витрина (без дублирования полного списка URL) |
| [roadmap.md](./roadmap.md) | Рекомендуемый технический бэклог и известные пробелы |
| [frontend.md](./frontend.md) | Практическое руководство для frontend: auth, CORS, сценарии, REST и STOMP |
| [testing.md](./testing.md) | Тесты: Testcontainers, JaCoCo, снятие с холда, пирамида, CI |
| [roles-product-journeys.md](./roles-product-journeys.md) | Продуктовые сценарии по ролям: регистрация, модерация, заявки, чаты; версии и ссылки на документацию зависимостей |
| [design-ui-brief.md](./design-ui-brief.md) | Бриф для UI/UX: чаты и страница проектов (данные, экраны, состояния) |
| [mail-setup.md](./mail-setup.md) | Подключение SMTP: переменные окружения, провайдеры, Mailpit |

Исходная конфигурация по умолчанию: `src/main/resources/application.yaml`. Интерактивная схема API: Swagger UI (`/swagger-ui.html`).
