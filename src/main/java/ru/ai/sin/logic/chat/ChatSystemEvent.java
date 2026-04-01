package ru.ai.sin.logic.chat;

/**
 * Коды системных сообщений в чате (хранятся в {@code chat_messages.system_event}).
 */
public final class ChatSystemEvent {

    public static final String REQUEST_SENT = "REQUEST_SENT";
    public static final String STUDENT_ACCEPTED = "STUDENT_ACCEPTED";
    public static final String STUDENT_REJECTED = "STUDENT_REJECTED";
    public static final String ADMIN_JOINED = "ADMIN_JOINED";

    private ChatSystemEvent() {}
}
