package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterNameReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.RecruiterMapper;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.service.impl.RecruiterService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterServImpl implements RecruiterService {

    private final RecruiterRepo recruiterRepo;

    private final RecruiterMapper recruiterMapper;

    private RecruiterEnt getActiveRecruiterOrThrow(UUID recruiterId) {
        RecruiterEnt recruiterEnt = recruiterRepo.findByIdAndIsActiveTrue(recruiterId);

        if (recruiterEnt == null) {
            throw new NotFoundException("Failed to find recruiter by id " + recruiterId);
        }

        return recruiterEnt;
    }

    @Override
    public RecruiterDTO getById(
            UUID id
    ) {
        return recruiterMapper.toDTO(getActiveRecruiterOrThrow(id));
    }

    @Override
    public List<RecruiterDTO> getAll(
            int pageRecruiterNumber, int pageRecruiterSize
    ) {
        Pageable pageable = PageRequest.of(pageRecruiterNumber, pageRecruiterSize);

        List<RecruiterEnt> recruiterEntList = recruiterRepo.findAllByIsActiveTrue(pageable).getContent();

        return recruiterEntList.stream().map(recruiterMapper::toDTO).toList();
    }

    @Override
    public List<RecruiterDTO> getAllByCompanyName(
            int pageRecruiterNumber, int pageRecruiterSize,
            GetRecruiterNameReq getRecruiterNameReq
    ) {
        Pageable pageable = PageRequest.of(pageRecruiterNumber, pageRecruiterSize);

        List<RecruiterEnt> recruiterEntList = recruiterRepo.findAllByCompanyNameIgnoreCaseAndIsActiveTrue(
                getRecruiterNameReq.companyName(),
                pageable
        ).getContent();

        return recruiterEntList.stream().map(recruiterMapper::toDTO).toList();
    }

    @Override
    public RecruiterDTO create(
            AddRecruiterReq addRecruiterReq
    ) {
        RecruiterEnt recruiterEnt = recruiterMapper.toEntity(addRecruiterReq);

        recruiterEnt = recruiterRepo.save(recruiterEnt);

        return recruiterMapper.toDTO(recruiterEnt);
    }

    @Override
    @Transactional
    public RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq
    ) {
        Optional<RecruiterEnt> recruiterEnt = recruiterRepo.findById(id);

        if (recruiterEnt.isEmpty()) {
            throw new NotFoundException("Failed to find recruiter by id " + id);
        }

        recruiterMapper.updateEntityFromDto(updateRecruiterReq, recruiterEnt.get());

        return recruiterMapper.toDTO(recruiterEnt.get());
    }

    @Override
    @Transactional
    public RecruiterDTO deleteById(
            UUID id
    ) {
        RecruiterEnt recruiterEnt = getActiveRecruiterOrThrow(id);

        recruiterEnt.setIsActive(false);

        return recruiterMapper.toDTO(recruiterEnt);
    }
}
