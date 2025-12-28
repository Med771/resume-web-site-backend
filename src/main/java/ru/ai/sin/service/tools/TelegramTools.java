package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.mapper.TelegramMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TelegramTools {

    private final StudentTools studentTools;
    private final RecruiterTools recruiterTools;

    private final TelegramMapper telegramMapper;

    @Transactional(readOnly = true)
    public StudentEnt getStudentOrThrow(UUID studentId) {
        return studentTools.getStudentOrThrow(studentId);
    }

    @Transactional(readOnly = true)
    public RecruiterEnt getRecruiterOrThrow(UUID recruiterId) {
        return recruiterTools.getRecruiterOrThrow(recruiterId);
    }

    public StudentTelegramDTO mapStudentToDTO(StudentEnt studentEnt) {
        return telegramMapper.studentToDTO(studentEnt);
    }

    public RecruiterTelegramDTO mapRecruiterToDTO(RecruiterEnt recruiterEnt) {
        return telegramMapper.recruiterToDTO(recruiterEnt);
    }
}

