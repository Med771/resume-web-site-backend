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

Telegram должен слать updates на **API-домен** (backend), не на www:

```
POST https://test2-api.singularity-resume.ru/telegram/webhook
Header: X-Telegram-Bot-Api-Secret-Token: <TELEGRAM_WEBHOOK_SECRET>
```

**Почему не `test2-www`:** www проксирует только статику SPA. `POST /telegram/webhook` на www даёт **405** или HTML — webhook не дойдёт до Spring. Нужен отдельный хост `*-api.*` с proxy на backend (порт 8001). Пример nginx: [../docs/nginx-test2.example.conf](../docs/nginx-test2.example.conf).

Установка webhook (один раз). **Выполняйте на сервере** — с рабочего ПК в РФ `api.telegram.org` часто недоступен (таймаут):

```bash
python3 tmp/set_telegram_webhook.py \
  --url https://test2-api.singularity-resume.ru/telegram/webhook
```

Переменные `TELEGRAM_BOT_TOKEN` и `TELEGRAM_WEBHOOK_SECRET` должны быть заданы в окружении backend (или переданы флагами `--token` / `--secret`).

Альтернатива — `curl` на сервере:

```bash
curl "https://api.telegram.org/bot<TOKEN>/setWebhook" \
  -d "url=https://test2-api.singularity-resume.ru/telegram/webhook" \
  -d "secret_token=<TELEGRAM_WEBHOOK_SECRET>"
```

Проверка:

```bash
curl "https://api.telegram.org/bot<TOKEN>/getWebhookInfo"
```

На API-хосте `POST /telegram/webhook` с JSON `{}` и заголовком секрета должен отвечать **200** (не 405 и не HTML фронта).

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
