package ru.ai.sin.dto.telegram;

/**
 * Ответ «кто по telegram user id»: студент или рекрутер и соответствующие данные.
 */
public record TelegramUserRes(
        String type,
        StudentTelegramDTO student,
        RecruiterTelegramDTO recruiter
) {
    public static final String TYPE_STUDENT = "STUDENT";
    public static final String TYPE_RECRUITER = "RECRUITER";

    public static TelegramUserRes student(StudentTelegramDTO dto) {
        return new TelegramUserRes(TYPE_STUDENT, dto, null);
    }

    public static TelegramUserRes recruiter(RecruiterTelegramDTO dto) {
        return new TelegramUserRes(TYPE_RECRUITER, null, dto);
    }
}
