# Настройка Telegram-бота для верификации телефона

Верификация используется при **саморегистрации студента**: пользователь указывает номер на сайте, подтверждает его через бота, затем завершает `POST /auth/register-student`.

## 1. Создание бота

1. Откройте [@BotFather](https://t.me/BotFather) в Telegram.
2. Команда `/newbot` → задайте имя и username (например `singularity_resume_bot`).
3. Сохраните **token** (формат `123456789:ABC...`).

Документация Telegram Bot API: https://core.telegram.org/bots/api

## 2. Переменные окружения

| Переменная | Пример | Описание |
|------------|--------|----------|
| `TELEGRAM_BOT_TOKEN` | из BotFather | Токен бота |
| `TELEGRAM_BOT_USERNAME` | `singularity_resume_bot` | Username **без** `@` |
| `TELEGRAM_WEBHOOK_SECRET` | случайная строка 32+ символов | Заголовок `X-Telegram-Bot-Api-Secret-Token` |
| `APP_TELEGRAM_ENABLED` | `true` | Включить верификацию |

В `application.yaml` маппинг: `app.telegram.enabled`, `app.telegram.bot-token`, и т.д.

## 3. Webhook (prod / staging)

Telegram должен слать updates на ваш backend:

```
POST https://api.singularity-resume.ru/telegram/webhook
Header: X-Telegram-Bot-Api-Secret-Token: <TELEGRAM_WEBHOOK_SECRET>
```

Установка webhook (один раз):

```bash
curl "https://api.telegram.org/bot<TOKEN>/setWebhook" \
  -d "url=https://api.singularity-resume.ru/telegram/webhook" \
  -d "secret_token=<TELEGRAM_WEBHOOK_SECRET>"
```

## 4. Локальная разработка

Telegram принимает webhook только по **HTTPS** с публичным URL. Варианты:

1. **ngrok / cloudflared** — туннель на `localhost:8080`, webhook на временный URL.
2. **Staging-сервер** — разработка верификации на уже развёрнутом API.

Без webhook и `APP_TELEGRAM_ENABLED=true` эндпоинт `POST /verification/phone/start` вернёт ошибку «Telegram-бот не настроен», **если не включён dev-режим** (см. ниже).

### Локальные тесты без бота

В `application.yaml` (только dev):

```yaml
app.telegram:
  allow-dev-confirm: true
  dev-confirm-code: "7890"
```

1. `POST /verification/phone/start` — создаёт сессию даже без настроенного бота.
2. `POST /verification/phone/{id}/confirm-code` с телом `{ "code": "7890" }` — статус `CONFIRMED`.
3. На фронте — 4 поля OTP на экране подтверждения.

**На prod обязательно:** `allow-dev-confirm: false`.

## 5. Поток для пользователя

1. Сайт: `POST /verification/phone/start` → ссылка `https://t.me/{bot}?start={verificationId}`.
2. Пользователь открывает бота → `/start {id}` → кнопка «Поделиться номером».
3. Бот сверяет номер с сессией → статус `CONFIRMED`.
4. Сайт опрашивает `GET /verification/phone/{id}/status`.
5. `POST /auth/register-student` с `phoneVerificationId`.

TTL сессии: `app.telegram.verification-ttl-minutes` (по умолчанию 15).

## 6. Безопасность

- Webhook без верного `secret_token` → **403**.
- Регистрация проверяет, что номер в заявке совпадает с подтверждённым.
- Email-верификация **не используется**.
