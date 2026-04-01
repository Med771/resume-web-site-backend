# Руководство для frontend-разработчика

## Базовый URL и CORS

- API должен быть в **`app.security.cors.allowed-origins`** (см. `application.yaml`), иначе браузер заблокирует запросы с **credentials**.
- Для сессии на cookie используйте **`credentials: 'include'`** (fetch) или **`withCredentials: true`** (axios).

## Вход в систему

1. `POST /auth/login` — JSON с учётными данными (точная схема — в Swagger, `LoginRequest`).
2. Ответ **204** + **Set-Cookie** — дальше браузер сам отправляет cookies на тот же origin API.
3. При **401** или истечении access: `POST /auth/refresh`, затем повтор запроса.
4. Выход: `POST /auth/logout`.

Имена cookie задаются в конфиге (часто **`ACCESS_TOKEN`**, **`REFRESH_TOKEN`**). Cookie **HttpOnly** — из JavaScript не читаются.

## Роли и экраны

| Роль | Типичные сценарии |
|------|-------------------|
| **GUEST** / **USER** | Каталог студентов, `POST /request`, `GET /recruiter/me`, чаты `/chat/...`. |
| **STUDENT** | `GET /student/me`, чаты, `POST /request/{id}/student-decision`. **Создавать заявку нельзя.** |
| **ADMIN** | Фильтр/удаление заявок, мягкое удаление сообщений, полная история чатов в REST. |

Spring ожидает authorities вида **`ROLE_*`**; это согласовано с данными пользователя в БД.

## Сценарий «рекрутер → студент»

1. Логин рекрутера (**USER** / **GUEST** / …).
2. Первая заявка — с полями компании и контактов (**`AddRequestReq`** в Swagger); далее чаще только **`studentId`**, если **`GET /recruiter/me`** уже возвращает профиль.
3. В **`RequestDTO`** есть **`appChatId`** и **`result`** — можно открыть чат по этому UUID.
4. До ответа студента в ленте REST — только **системные** сообщения; ориентир для UI: `messageKind`, `systemEvent` (`REQUEST_SENT`, `STUDENT_ACCEPTED`, `STUDENT_REJECTED`, `ADMIN_JOINED` — см. backend-док).
5. Чтобы вызвать **`POST /request/{id}/student-decision`**, нужен числовой **`id` заявки**. Пока отдельного списка заявок для студента в API может не быть — см. [roadmap.md](./roadmap.md); временно не полагайтесь только на парсинг текста системного сообщения.

## REST чата

Базовый префикс: **`/chat`**.

| Метод | Путь | Назначение |
|-------|------|------------|
| GET | `/chat` | Список чатов (`PageResponse<ChatSummaryDTO>`) |
| GET | `/chat/{chatId}/summary` | Сводка |
| GET | `/chat/{chatId}/messages` | Страница сообщений |
| POST | `/chat/{chatId}/messages` | Текст: `{ "body": "..." }` |
| POST | `/chat/{chatId}/messages/attachment` | `multipart/form-data`: часть **`file`**, опционально **`body`** |
| PATCH | `/chat/{chatId}/messages/{messageId}` | Новое тело |
| POST | `/chat/{chatId}/read` | `{ "messageId": "<uuid>" }` |
| DELETE | `/chat/{chatId}/messages/{messageId}` | Только админ, мягкое удаление |

Параметры пагинации Spring: **`page`**, **`size`** (и при необходимости `sort` — уточняйте по Swagger для конкретного метода).

## WebSocket (STOMP + SockJS)

- Подключение к **`/ws`** через SockJS (см. документацию Spring WebSocket).
- Подписки:
  - **`/topic/chats/{appChatId}`** — обязательна для рекрутера/студента/админа: системные события и полная переписка **после** принятия заявки.
  - **`/topic/chats/{appChatId}/staff`** — для **админского** UI, если нужен real-time по **пользовательским** сообщениям **до** принятия заявки (они туда не попадают в общий топик намеренно).

После события по WS имеет смысл дозапрашивать `GET .../messages` для согласованности.

**Важно:** в текущей конфигурации handshake **`/ws`** может быть доступен без JWT на уровне Spring Security; в продакшене контракт может усилиться — закладывайте передачу токена при connect, если появится требование.

## Решение студента по заявке

`POST /request/{id}/student-decision`

```json
{
  "accept": true,
  "comment": "опционально"
}
```

или `"accept": false`. Успех — обновить заявку/чат и обработать новые WS-сообщения.

## Swagger

Точные поля DTO, коды ответов и **`@PreAuthorize`** — в **Swagger UI** (`/swagger-ui.html`).

## Частые ошибки

- Запросы без **credentials** — постоянные 401.
- Origin не из CORS — ошибка в консоли браузера без нормального тела ответа.
- Студент вызывает **`POST /request`** — отказ в доступе.
- Ожидание **пользовательских** сообщений у рекрутера/студента **до** accept: в REST их нет; в WS до принятия они уходят в **`/staff`**, не в общий топик.
