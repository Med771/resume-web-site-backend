CREATE TABLE students
(
    id                UUID                 NOT NULL,
    city              VARCHAR(255),
    hh_link           VARCHAR(255),
    birth_date        date                 NOT NULL,
    bio               TEXT,
    image_path        VARCHAR(255),
    is_active         BOOLEAN DEFAULT TRUE NOT NULL,
    course            VARCHAR(16),
    busyness          VARCHAR(32),
    speciality_id     BIGINT,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    username          VARCHAR(64),
    email             VARCHAR(255),
    password_hash     VARCHAR(128),
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    phone_number      VARCHAR(16),
    telegram_username VARCHAR(32),
    telegram_user_id  VARCHAR(16),
    CONSTRAINT pk_students PRIMARY KEY (id)
);

ALTER TABLE students
    ADD CONSTRAINT uc_students_imagepath UNIQUE (image_path);

ALTER TABLE students
    ADD CONSTRAINT uc_students_telegram_user UNIQUE (telegram_user_id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_SPECIALITY FOREIGN KEY (speciality_id) REFERENCES specialities (id);