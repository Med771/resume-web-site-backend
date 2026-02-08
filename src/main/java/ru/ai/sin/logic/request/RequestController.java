package ru.ai.sin.logic.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.request.dto.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/request")
public class RequestController {

    private final RequestService requestService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(requestService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<RequestDTO>> getByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @Valid @RequestBody FilterRequestReq filterRequestReq) {
        return ResponseEntity.ok(requestService.getByFilter(pageable, filterRequestReq));
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<RequestDTO> create(@Valid @RequestBody AddRequestReq addRequestReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.create(addRequestReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        requestService.deleteById(id);
    }
}
