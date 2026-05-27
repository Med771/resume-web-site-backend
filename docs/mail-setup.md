# Подключение почты

Бэкенд готов к отправке писем через SMTP (Spring Mail). Подтверждение регистрации и сброс пароля подключаются отдельно — сейчас есть только инфраструктура `EmailService`.

## Что уже есть в коде

| Компонент | Назначение |
|-----------|------------|
| `app.mail.*` | Включение, адрес отправителя, URL фронта для ссылок в письмах |
| `spring.mail.*` | Параметры SMTP-сервера |
| `EmailService.sendAsync(...)` | Асинхронная отправка; при `enabled=false` письма не уходят, в лог пишется строка |

Пример конфигурации: [`application-mail.example.yaml`](../src/main/resources/application-mail.example.yaml).

---

## Что нужно подготовить (чеклист)

1. **Почтовый ящик или сервис** для исходящих писем (один адрес «от кого», например `noreply@singularity-resume.ru`).
2. **Данные SMTP** от провайдера:
   - хост (например `smtp.yandex.ru`, `smtp.mail.ru`, `smtp.gmail.com`);
   - порт (**587** с STARTTLS или **465** с SSL);
   - логин (часто = email);
   - пароль (**не** обычный пароль от почты, если провайдер требует «пароль приложения»).
3. **DNS для домена** (продакшен): SPF, DKIM, при желании DMARC — иначе письма часто попадают в спам.
4. **URL фронта** — куда вести пользователя из письма (`https://singularity-resume.ru`).

---

## Переменные окружения

| Переменная | Пример | Описание |
|------------|--------|----------|
| `MAIL_ENABLED` | `true` | Включить реальную отправку |
| `MAIL_HOST` | `smtp.yandex.ru` | SMTP-сервер |
| `MAIL_PORT` | `587` | Порт |
| `MAIL_USERNAME` | `noreply@singularity-resume.ru` | Логин SMTP |
| `MAIL_PASSWORD` | *(секрет)* | Пароль / пароль приложения |
| `MAIL_FROM_ADDRESS` | `noreply@singularity-resume.ru` | From в письме |
| `MAIL_FROM_NAME` | `Singularity Resume` | Имя отправителя |
| `MAIL_FRONTEND_BASE_URL` | `https://singularity-resume.ru` | База ссылок для писем |

Локально по умолчанию `MAIL_ENABLED=false` — приложение стартует без SMTP.

---

## Как подключить

### Вариант A. Продакшен / staging (переменные окружения)

На сервере задайте переменные из таблицы выше и перезапустите приложение.

Проверка: в логах при отправке (когда появится сценарий с письмом) должна быть строка `Mail sent: to=...`.

### Вариант B. Локально с реальным SMTP

В `application.yaml` или профиле:

```yaml
app:
  mail:
    enabled: true

spring:
  mail:
    host: smtp.yandex.ru
    port: 587
    username: your@domain.ru
    password: your-app-password
```

Либо только env: `MAIL_ENABLED=true` и остальные `MAIL_*`.

### Вариант C. Локально без реальной почты (Mailpit)

1. Запустите [Mailpit](https://github.com/axllent/mailpit) (Docker):

   ```bash
   docker run -d --name mailpit -p 8025:8025 -p 1025:1025 axllent/mailpit
   ```

2. Настройки:

   | Переменная | Значение |
   |------------|----------|
   | `MAIL_ENABLED` | `true` |
   | `MAIL_HOST` | `localhost` |
   | `MAIL_PORT` | `1025` |
   | `MAIL_USERNAME` | *(пусто)* |
   | `MAIL_PASSWORD` | *(пусто)* |

3. Веб-интерфейс писем: http://localhost:8025

Для Mailpit в `application.yaml` можно отключить auth/starttls, если соединение не поднимается — задайте в профиле `spring.mail.properties.mail.smtp.auth: false`.

---

## Типичные провайдеры

| Провайдер | Хост | Порт | Примечание |
|-----------|------|------|------------|
| Yandex 360 / Почта | `smtp.yandex.ru` | 587 | Пароль приложения в настройках безопасности |
| Mail.ru | `smtp.mail.ru` | 587 | Пароль для внешних приложений |
| Google Workspace | `smtp.gmail.com` | 587 | App Password, 2FA обязательна |
| SendGrid | `smtp.sendgrid.net` | 587 | Логин `apikey`, пароль = API key |
| UniSender / др. | см. документацию | 587/465 | Обычно отдельный SMTP в кабинете |

Точные значения всегда берите из кабинета провайдера.

---

## Безопасность

- Пароль SMTP **не** коммитьте в git — только env или секреты CI/CD.
- В репозитории уже есть `application-mail.example.yaml` без реальных секретов.
- Для продакшена используйте отдельный ящик `noreply@...`, не личную почту разработчика.

---

## Следующий шаг (не в этой задаче)

Когда понадобится подтверждение email:

1. Таблица токенов + эндпоинты verify/resend.
2. Вызов `EmailService.sendAsync(...)` из регистрации.
3. Ссылки вида `{MAIL_FRONTEND_BASE_URL}/verify-email?token=...`.

`MailProperties.frontendBaseUrl` уже зарезервирован под такие ссылки.
