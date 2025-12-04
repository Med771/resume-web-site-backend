ALTER TABLE skills
    ADD student_id UUID;

ALTER TABLE skills
    ADD CONSTRAINT FK_SKILLS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (id);