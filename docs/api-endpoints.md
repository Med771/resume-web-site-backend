# Справочник HTTP API

Актуально для кода в репозитории. **`server.servlet.context-path` не задан** — пути от корня хоста (например `https://api.example.com/auth/login`).

- **Аутентификация:** JWT в **HttpOnly cookie** (имена из `application.yaml`, обычно `ACCESS_TOKEN`, `REFRESH_TOKEN`). Для браузера: `credentials: 'include'`.
- **Роли в `@PreAuthorize`:** `GUEST`, `USER`, `STUDENT`, `ADMIN` (в токене/Principal — префикс `ROLE_`).
- **Пагинация Spring Data:** query `page`, `size` (0-based). Параметр **`sort` в query для эндпоинтов с `Pageable` не используется** там, где в Swagger указано «без параметра sort» или для списков студентов (сортировка — в теле `FilterStudentReq`).
- **Детали DTO, коды ошибок:** [Swagger UI](http://localhost:8080/swagger-ui.html) (`/swagger-ui.html`, `/v3/api-docs`).

---

## Публичные пути (`permitAll`, без JWT)

| Метод | Путь | Назначение |
|-------|------|------------|
| POST | `/auth/register-recruiter` | Заявка на регистрацию работодателя; **204**, cookie не выдаются |
| POST | `/auth/register-student` | Саморегистрация студента; **204** + Set-Cookie (см. `app.registration`) |
| POST | `/auth/login` | Вход; **204** + Set-Cookie |
| POST | `/auth/refresh` | Новый access; **204** + Set-Cookie |
| POST | `/auth/logout` | Очистка cookie; **204** |
| GET | `/public/registration/specialities` | Справочник специальностей; query `page`, `size` (лимит `app.registration.max-catalog-page-size`) |
| GET | `/public/registration/skills` | Справочник навыков |
| GET | `/public/registration/companies` | Справочник компаний |
| GET | `/public/registration/educations` | Справочник образования |
| GET | `/public/students/{id}` | Витрина: **укороченная** карточка при `publicProfileConsent` и курс ≠ `NEW` |
| POST | `/public/students/cards` | Витрина: страница карточек; тело `FilterStudentReq` (опционально `null`); сортировка только в JSON |
| GET | `/public/projects` | Лента проектов для анонимов (`visibleToAnonymous`, окна публикации) |
| POST | `/public/analytics/events` | Запись события аналитики; **204**; при лимите IP — **429** |
| GET | `/main/status` | Liveness; **204** |
| GET | `/main/photo/{image_path}` | Байты файла изображения из хранилища |
| GET | `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**` | OpenAPI / UI |
| OPTIONS | `/**` | CORS preflight |

WebSocket handshake: **`/ws/**`** также `permitAll` на уровне HTTP (см. [backend.md](./backend.md)).

---

## `/auth` — сессия и регистрация

Все методы выше в таблице публичных. Кратко:

| Метод | Путь | Ответ | Примечание |
|-------|------|-------|------------|
| POST | `/auth/register-recruiter` | 204 | Тело `RecruiterSelfRegistrationReq` |
| POST | `/auth/register-student` | 204 | Тело `StudentSelfRegistrationReq`; cookie как после login |
| POST | `/auth/login` | 204 | `LoginRequest` |
| POST | `/auth/refresh` | 204 | Refresh из cookie |
| POST | `/auth/logout` | 204 | Очистка обеих cookie |

---

## `/main` — служебное и файлы

| Метод | Путь | Доступ | Описание |
|-------|------|--------|----------|
| GET | `/main/status` | публично | **204** — сервис жив |
| GET | `/main/photo/{image_path}` | публично | Тело: байты изображения, заголовок `Content-Type` |

---

## `/public/registration` — справочники для формы регистрации

Все **GET**, публично. Пагинация: `page`, `size` (размер ограничен конфигом).

| Путь | Ответ |
|------|--------|
| `/public/registration/specialities` | `PageResponse<SpecialityDTO>` |
| `/public/registration/skills` | `PageResponse<SkillDTO>` |
| `/public/registration/companies` | `PageResponse<CompanyDTO>` |
| `/public/registration/educations` | `PageResponse<EducationDTO>` |

---

## `/public/students` — витрина без входа

| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/public/students/{id}` | `StudentCardDTO`; **404** если нет согласия / `NEW` / нет записи |
| POST | `/public/students/cards` | `PageResponse<StudentCardDTO>`; тело `FilterStudentReq` (можно опустить); сервер дополнительно требует `publicProfileConsent` и курс ≠ `NEW`; сортировка: `sortBy` (в т.ч. `MANUAL_SORT_ORDER`), `sortDirection`, `useDefaultRanking` (релевантность: `manualSortOrder` → аватар → score → дата) |

---

## `/public/projects`

| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/public/projects` | Список `SiteProjectDTO` для анонимов (фильтр видимости и дат на сервере) |

---

## `/public/analytics`

| Метод | Путь | Описание |
|-------|------|----------|
| POST | `/public/analytics/events` | **204**; тело `AnalyticsEventInReq`; rate limit по IP (`app.analytics`) |

---

## `/student` — карточки (вход обязателен)

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| GET | `/student/me` | **STUDENT** | Своя карточка `StudentDTO`; **404** если нет привязки |
| GET | `/student/{id}` | **GUEST**, **USER**, **ADMIN** | Полный `StudentDTO`; курс **NEW** для не-админа → **404** |
| POST | `/student/cardsFilter` | **GUEST**, **USER**, **ADMIN** | `PageResponse<StudentCardDTO>`; тело `FilterStudentReq`; **NEW** только в выдаче у админа; сортировка только из JSON (релевантность начинается с `manualSortOrder` ASC) |
| POST | `/student/filter` | **GUEST**, **USER**, **ADMIN** | `PageResponse<StudentDTO>`; те же правила |
| POST | `/student/photo/{id}` | **ADMIN** | `multipart/form-data`, часть **`avatarFile`**; **204** |
| POST | `/student` | **ADMIN** | Создание; **201**; опционально **`publicProfileConsent`**, **`manualSortOrder`** в теле `AddStudentReq` |
| POST | `/student/extended` | **ADMIN** | Создание с вложенными сущностями; **201**; те же опциональные поля, что и в `AddStudentReq` |
| PUT | `/student/{id}` | **ADMIN** | Полное обновление `UpdateStudentReq`; **200**; ручной порядок: `manualSortOrder` / `clearManualSortOrder` |
| PATCH | `/student/{id}` | **ADMIN** | Частичное `PatchStudentReq`; **200**; то же для `manualSortOrder` |
| DELETE | `/student/{id}` | **ADMIN** | Каскадное удаление связанных данных; **204** |

Роль **STUDENT** к `GET /student/{id}`, `POST /student/cardsFilter`, `POST /student/filter` **не** допускается (каталог через эти пути студенту недоступен).

---

## `/request` — заявки

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| GET | `/request/{id}` | **ADMIN** | `RequestDTO` |
| POST | `/request/filter` | **ADMIN** | Страница заявок по `FilterRequestReq` |
| POST | `/request` | **GUEST**, **USER**, **ADMIN** | Создание заявки рекрутером; **STUDENT** — **403**; **201**; студент **NEW** для не-админа — **404** |
| POST | `/request/{id}/student-decision` | **STUDENT** | Тело `StudentRequestDecisionReq`; **204** |
| DELETE | `/request/{id}` | **ADMIN** | **204** |

---

## `/chat` — REST чата

Все требуют **`isAuthenticated()`**, кроме удаления сообщения.

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| GET | `/chat` | любой аутентифицированный | Список чатов с превью (`PageResponse<ChatSummaryDTO>`) |
| GET | `/chat/{chatId}/summary` | любой | Сводка чата |
| GET | `/chat/{chatId}/messages` | любой | Страница сообщений (видимость USER/SYSTEM по правилам домена) |
| POST | `/chat/{chatId}/messages` | любой | Текст; **201** `ChatMessageDTO`; тело `PostChatMessageReq` |
| POST | `/chat/{chatId}/messages/attachment` | любой | `multipart`: часть **`file`**, опционально **`body`**; **201** |
| PATCH | `/chat/{chatId}/messages/{messageId}` | любой | Редактирование; `PatchChatMessageReq` |
| POST | `/chat/{chatId}/read` | любой | Отметка прочитанного; `MarkChatReadReq`; **204** |
| DELETE | `/chat/{chatId}/messages/{messageId}` | **ADMIN** | Мягкое удаление; **204** |

Подробности видимости сообщений: [backend.md](./backend.md).

---

## `/recruiter`

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| GET | `/recruiter/me` | **GUEST**, **USER**, **ADMIN** | Свой профиль или **404** (до первой заявки с данными) |
| GET | `/recruiter/{id}` | **GUEST**, **USER**, **ADMIN** | Карточка по UUID |
| POST | `/recruiter` | **ADMIN** | Создание; **201** |
| POST | `/recruiter/filter` | **ADMIN** | Фильтр страниц |
| PUT | `/recruiter/{id}` | **ADMIN** | Полное обновление |
| PATCH | `/recruiter/{id}` | **ADMIN** | Частичное обновление |
| DELETE | `/recruiter/{id}` | **ADMIN** | **204** |

---

## `/user` — пользователи (админ)

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| POST | `/user/filter` | **ADMIN** | `FilterUserReq` |
| POST | `/user` | **ADMIN** | Создание; для ЛК студента: роль **STUDENT** + `studentId`; **201** |
| DELETE | `/user/{id}` | **ADMIN** | Удаление **USER** / **STUDENT** по UUID; **204** |

---

## `/admin/site-projects` — лента проектов (админ)

Класс контроллера: `@PreAuthorize("hasRole('ADMIN')")` на все методы.

| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/admin/site-projects` | Все проекты в порядке `sortOrder` |
| POST | `/admin/site-projects` | Создание; **201** `CreateSiteProjectReq` |
| PUT | `/admin/site-projects/{id}` | Обновление `UpdateSiteProjectReq` |
| DELETE | `/admin/site-projects/{id}` | **204** |
| POST | `/admin/site-projects/reorder` | Порядок `orderedIds`; **204** |

---

## `/admin/analytics` — отчёты (админ)

| Метод | Путь | Описание |
|-------|------|----------|
| POST | `/admin/analytics/summary` | Тело `AnalyticsSummaryReq`; ответ `AnalyticsSummaryDTO` (события `PAGE_VIEW` по `path`) |
| POST | `/admin/analytics/entity-population` | Тело опционально `EntityPopulationSummaryReq` (`from`/`to` вместе или оба `null`); ответ `EntityPopulationSummaryDTO` — пользователи по ролям, всего студентов/рекрутеров, при окне — новые по `created_at` |

---

## `/admin/recruiter-registration-requests` — модерация регистрации работодателей

Класс: `@PreAuthorize("hasRole('ADMIN')")`.

| Метод | Путь | Описание |
|-------|------|----------|
| POST | `/admin/recruiter-registration-requests/filter` | Тело опционально `FilterRecruiterRegistrationReq` |
| POST | `/admin/recruiter-registration-requests/{id}/approve` | Одобрение; ответ `RecruiterRegistrationApproveResultDTO` |
| POST | `/admin/recruiter-registration-requests/{id}/reject` | Отклонение; опционально тело `RecruiterRegistrationRejectReq`; **204** |

---

## Справочники: `/company`, `/skill`, `/speciality`, `/education`

Паттерн одинаковый:

| Метод | Путь | Роли |
|-------|------|------|
| GET | `/{resource}/{id}` | **GUEST**, **USER**, **ADMIN** |
| POST | `/{resource}/filter` | **ADMIN** |
| POST | `/{resource}` | **ADMIN** |
| PUT | `/{resource}/{id}` | **ADMIN** |
| DELETE | `/{resource}/{id}` | **ADMIN** |

`{resource}` ∈ `company`, `skill`, `speciality`, `education`. Тела — DTO из пакета `…dto` соответствующего модуля.

---

## `/experience` — опыт работы

| Метод | Путь | Роли |
|-------|------|------|
| GET | `/experience/{id}` | **GUEST**, **USER**, **ADMIN** |
| POST | `/experience/filter` | **GUEST**, **USER**, **ADMIN** |
| POST | `/experience` | **ADMIN** |
| PUT | `/experience/{id}` | **ADMIN** |
| DELETE | `/experience/{id}` | **ADMIN** |

---

## `/portfolio` — портфолио

| Метод | Путь | Роли |
|-------|------|------|
| GET | `/portfolio/{id}` | **GUEST**, **USER**, **ADMIN** |
| POST | `/portfolio/filter` | **GUEST**, **USER**, **ADMIN** |
| POST | `/portfolio` | **ADMIN** |
| PUT | `/portfolio/{id}` | **ADMIN** |
| DELETE | `/portfolio/{id}` | **ADMIN** |

---

## `/institution` — учёба студента (связь студент–образование)

| Метод | Путь | Роли | Примечание |
|-------|------|------|------------|
| GET | `/institution/{id}` | **GUEST**, **USER**, **ADMIN** | |
| POST | `/institution/filter` | **GUEST**, **USER**, **ADMIN** | Если в фильтре передан `educationId`, внутри вызывается проверка **админа** (`SecurityHelper.checkAdminRoleForFilter`) |
| POST | `/institution` | **ADMIN** | |
| PUT | `/institution/{id}` | **ADMIN** | |
| DELETE | `/institution/{id}` | **ADMIN** | |

---

## Сводка по доступу к «фильтрам» справочников

| Ресурс | `POST …/filter` для рекрутера (GUEST/USER) |
|--------|---------------------------------------------|
| company, skill, speciality, education | Нет, только **ADMIN** |
| experience, portfolio, institution | Да (**GUEST**, **USER**, **ADMIN**) |

---

## WebSocket (не HTTP REST)

| Назначение | Путь / префикс |
|------------|----------------|
| SockJS | `/ws` |
| STOMP broker | подписки на `/topic/...` |
| Основной топик чата | `/topic/chats/{chatId}` |
| Сообщения до принятия заявки (для админского UI) | `/topic/chats/{chatId}/staff` |

`{chatId}` — UUID прикладного чата (`appChatId` в `RequestDTO`).

---

## Связанные документы

- [backend.md](./backend.md) — домен, чаты, файлы, релизный чеклист  
- [frontend.md](./frontend.md) — CORS, cookie, типичные ошибки  
- [roles-product-journeys.md](./roles-product-journeys.md) — продуктовые сценарии  
- [project-passport.md](./project-passport.md) — общий паспорт проекта  

При изменении контроллеров обновляйте этот файл и при необходимости аннотации `@Operation` в коде.
