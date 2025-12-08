ALTER TABLE applications
    ADD CONSTRAINT uc_applications_chat UNIQUE (chat_id);

ALTER TABLE applications
    ADD CONSTRAINT uc_applications_history_path UNIQUE (history_path);

ALTER TABLE applications
    ADD CONSTRAINT FK_APPLICATIONS_ON_RECRUITER FOREIGN KEY (recruiter_id) REFERENCES recruiters (id);

ALTER TABLE applications
    ADD CONSTRAINT FK_APPLICATIONS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (id);
