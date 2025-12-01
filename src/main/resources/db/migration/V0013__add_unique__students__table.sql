ALTER TABLE students
    ADD CONSTRAINT uc_students_email UNIQUE (email);

ALTER TABLE students
    ADD CONSTRAINT uc_students_username UNIQUE (username);