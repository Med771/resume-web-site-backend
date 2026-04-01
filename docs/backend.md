# Устройство и возможности backend

## Стек и инфраструктура

| Компонент | Назначение |
|-----------|------------|
| **Java 21**, **Spring Boot 3.5.x** | REST API, безопасность, JPA |
| **PostgreSQL** | основное хранилище |
| **Flyway** | миграции (`classpath:db/migration`, `out-of-order: true`) |
| **Spring Data JPA** | репозитории, спецификации для фильтров |
| **JJWT** (через `JwtHelper`) | access / refresh токены |
| **Spring WebSocket + STOMP** | простой брокер сообщений, SockJS endpoint |
| **springdoc-openapi** | Swagger UI (`/swagger-ui.html`, `/v3/api-docs`) |

Конфигурация по умолчанию в `application.yaml`: datasource (например `jdbc:postgresql://localhost:5501/resume`), JWT в **HttpOnly cookies** (`ACCESS_TOKEN`, `REFRESH_TOKEN`), CORS с `allow-credentials: true`, путь cookie `/`.

## Доменная модель (упрощённо)

- **Компании, институты, специальности, навыки** — справочники и CRUD (см. соответствующие контроллеры).
- **Студенты** (`students` + связанные сущности: опыт, портфолио, образование и т.д.) — карточки резюме, фильтры, загрузка фото.
- **Рекрутеры** — отдельные сущности; к пользователю привязка через `users.recruiter_id`.
- **Пользователи** (`users`) — роль, опционально связь **1:1** со студентом (`users.student_id`, уникальна).
- **Заявки** (`requests`) — связь рекрутер + студент + **`app_chat_id`** (чат в приложении), результат (`result`), текст ответа студента.
- **Чаты** (`chats`) — один чат на пару **рекрутер–студент** (уникальный индекс по паре).
- **Сообщения** (`chat_messages`) — вид сообщения (`USER` / `SYSTEM`), тело, вложение (имя файла в хранилище), мягкое удаление админом.
- **Прочитанность** (`chat_read_states`) — последнее прочитанное сообщение по паре (чат, пользователь).

Миграция **V0029** вводит чаты/сообщения/read state, `users.student_id`, переносит заявки на `app_chat_id`, убирает старые Telegram-поля с заявок.

## Роли и безопасность

Роли: **`GUEST`**, **`USER`**, **`STUDENT`**, **`ADMIN`**. В Spring Security для `hasRole('X')` в JWT/Principal ожидается authority вида **`ROLE_X`** (формируется в `UserHelper` из `RoleEnum`).

В `SecurityConfig`: кроме явных исключений всё требует **аутентификации**. Исключения: `/auth/**`, `/main/**`, **`/ws/**`** (handshake WebSocket), Swagger, `/error`, `OPTIONS /**`.

**`JwtCookieAuthenticationFilter`**: читает access (и при необходимости refresh), валидирует JWT, поднимает `SecurityContext` с `UserDetails` по username из БД.

## Основные возможности по областям

### Аутентификация (`/auth`)

- `POST /auth/login` — тело с логином/паролем, в ответе **Set-Cookie** для access и refresh.
- `POST /auth/refresh` — новый access по refresh cookie.
- `POST /auth/logout` — очистка cookie.

### Публичное / служебное (`/main`)

- `GET /main/status` — проверка живости.
- `GET /main/photo/{image_path}` — отдача файла картинки (аватары и т.д.).

### Заявки (`/request`)

- Админ: `GET /request/{id}`, `POST /request/filter`, `DELETE /request/{id}`.
- Рекрутер (роли **`GUEST`/`USER`/`ADMIN`**, не **`STUDENT`**): `POST /request` — создание заявки; чат get-or-create, системное сообщение **`REQUEST_SENT`**, ожидание решения (**`WAITING`** / в проверке решения студента также учитывается **`CREATION`**).
- Студент: `POST /request/{id}/student-decision` — `StudentRequestDecisionReq`: `accept` (boolean), опционально `comment`; **`STUDENT_CONFIRMED`** или **`REFUSAL`**, в чат — **`STUDENT_ACCEPTED`** / **`STUDENT_REJECTED`**.

### Чаты (`/chat`)

Все операции под `isAuthenticated()` или явной ролью админа где указано.

- Список «моих» чатов с превью и непрочитанным (админ — все; иначе по привязке рекрутер/студент).
- `GET /chat/{chatId}/summary`, `GET /chat/{chatId}/messages` (постранично).
- Отправка текста и **multipart** с вложением.
- Редактирование сообщения (свои; админ — по правилам сервиса).
- Админ: мягкое удаление сообщения.
- Отметка прочитанного по `messageId`.

### Бизнес-логика видимости чата

- Пока по паре рекрутер–студент **нет** «разрешённого» результата заявки (**`STUDENT_CONFIRMED`**, **`SUCCESS`**, **`RECRUITER_CONFIRMED`**), **рекрутер и студент** в REST видят **только системные** сообщения. Обычные сообщения участников в этот период **не показываются** в списке и в превью.
- **Админ** видит полную историю.
- После принятия заявки студентом — полная переписка для сторон (при доступе к чату).
- Первое **пользовательское** сообщение **админа** в чате инициирует системное **`ADMIN_JOINED`** (`ChatServiceImpl`).

### WebSocket

- Endpoint: **`/ws`** (SockJS).
- Брокер: префикс **`/topic`**.
- **`/topic/chats/{chatId}`** — системные сообщения и (после «разрешения» заявки) пользовательские для общего топика.
- До принятия заявки **пользовательские** сообщения публикуются в **`/topic/chats/{chatId}/staff`** — для real-time админу; рекрутер/студент подписываются только на основной топик.

Подробнее для клиентов: [frontend.md](./frontend.md).

### Пользователи (`/user`, админ)

Фильтр, создание (в т.ч. **`STUDENT`** + `studentId`), удаление **`USER`** / **`STUDENT`**.

### Студент (`/student`)

Публичные/гостевые выборки, админские изменения; ЛК: **`GET /student/me`** при роли **`STUDENT`**.

### Рекрутер (`/recruiter`)

В т.ч. **`GET /recruiter/me`** — привязанный профиль (404, если ещё нет; первая заявка с полными данными может создать и привязать профиль).

Остальные контроллеры (**company, institution, education, experience, portfolio, skill, speciality**) — CRUD/фильтры; матрица доступа в аннотациях контроллеров и в Swagger.

## Хранение файлов

`app.file.path` (по умолчанию `file`) и лимит размера — вложения чата и фото студентов на диске; картинки — также через `/main/photo/...`.

## Документация API

Swagger UI и OpenAPI — см. `springdoc` и `app.swagger` в `application.yaml`.

## Системные события чата (строки `systemEvent`)

Константы в `ru.ai.sin.logic.chat.ChatSystemEvent`:

- `REQUEST_SENT`
- `STUDENT_ACCEPTED`
- `STUDENT_REJECTED`
- `ADMIN_JOINED`
