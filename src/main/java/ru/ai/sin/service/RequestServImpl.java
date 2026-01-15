package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.request.AddRequestReq;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;

import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.spec.RequestSpecifications;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.repository.RequestRepo;

import ru.ai.sin.service.impl.RequestService;
import ru.ai.sin.service.tools.RecruiterTools;
import ru.ai.sin.service.tools.RequestTools;
import ru.ai.sin.service.tools.StudentTools;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServImpl implements RequestService {

    private final RequestRepo requestRepo;

    private final RequestTools requestTools;
    private final StudentTools studentTools;
    private final RecruiterTools recruiterTools;

    @Override
    @Transactional(readOnly = true)
    public RequestDTO getById(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        return requestTools.mapToDTO(requestEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RequestDTO> getByFilter(Pageable pageable, RequestFilterReq requestFilterReq) {
        Page<RequestEnt> page = requestRepo
                .findAll(
                        RequestSpecifications.byFilters(requestFilterReq),
                        pageable
                );

        return new PageResponse<>(
                page.getContent().stream().map(requestTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public RequestDTO create(AddRequestReq addRequestReq) {
        AddRecruiterReq addRecruiterReq = new AddRecruiterReq(
                addRequestReq.companyName(),
                addRequestReq.firstName(),
                addRequestReq.lastName(),
                addRequestReq.username(),
                addRequestReq.email(),
                addRequestReq.phoneNumber(),
                addRequestReq.telegramUsername()
        );

        var recruiterEnt = recruiterTools.findOrCreateRecruiter(addRecruiterReq);

        StudentEnt studentEnt = studentTools.getStudentOrThrow(addRequestReq.studentId());

        RequestEnt requestEnt = new RequestEnt();
        requestEnt.setRecruiter(recruiterEnt);
        requestEnt.setStudent(studentEnt);

        try {
            requestEnt = requestRepo.save(requestEnt);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Error while creating request: {}", ex.getMessage());
            throw new BadRequestException("Error while creating request");
        }

        RequestDTO requestDTO = requestTools.mapToDTO(requestEnt);
        log.info("Created new request: {} for recruiter: {} and student: {}", requestEnt.getId(), recruiterEnt.getId(), studentEnt.getId());

        return requestDTO;
    }

    @Override
    @Transactional
    public RequestDTO updateStatus(long id, RequestUpdateStatusReq requestUpdateStatusReq) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        requestEnt.setResult(requestUpdateStatusReq.resultEnum());

        requestEnt = requestRepo.save(requestEnt);

        RequestDTO requestDTO = requestTools.mapToDTO(requestEnt);

        log.info("Updated request status: {} with status: {}", id, requestUpdateStatusReq.resultEnum());

        return requestDTO;
    }
}
