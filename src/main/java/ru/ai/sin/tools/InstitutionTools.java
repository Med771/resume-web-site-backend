package ru.ai.sin.tools;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.ai.sin.entity.InstitutionEnt;
import ru.ai.sin.repository.InstitutionRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstitutionTools {

    private final InstitutionRepo institutionRepo;

    @Transactional()
    public List<Long> getInstitutionIdsByEducationId(
            Long educationId,
            int pageInstitutionNumber, int pageInstitutionSize) {
        Page<InstitutionEnt> institutions = institutionRepo.findAllByEducationId(
                educationId, PageRequest.of(pageInstitutionNumber, pageInstitutionSize));

        return institutions.getContent().stream().map(InstitutionEnt::getId).toList();
    }
}
