CREATE TABLE employees
(
    id                UUID                 NOT NULL,
    company_name      VARCHAR(255)         NOT NULL,
    is_active         BOOLEAN DEFAULT TRUE NOT NULL,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    username          VARCHAR(64),
    email             VARCHAR(255),
    password_hash     VARCHAR(128),
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    phone_number      VARCHAR(32),
    telegram_username VARCHAR(32),
    telegram_user_id  VARCHAR(32),
    CONSTRAINT pk_employees PRIMARY KEY (id)
);

ALTER TABLE employees
    ADD CONSTRAINT uc_employees_telegram_user UNIQUE (telegram_user_id);