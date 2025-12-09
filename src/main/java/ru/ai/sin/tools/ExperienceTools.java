package ru.ai.sin.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.entity.ExperienceEnt;
import ru.ai.sin.repository.ExperienceRepo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceTools {

    private final ExperienceRepo experienceRepo;


}
