package ru.ai.sin.logic.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.student.dto.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable @NotNull UUID id) {
        StudentDTO studentDTO = studentService.getById(id);

        return ResponseEntity.ok(studentDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/cardsFilter")
    public ResponseEntity<PageResponse<StudentCardDTO>> getCardsAllByFilters(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterStudentReq filterStudentReq
    ) {
        PageResponse<StudentCardDTO> studentCardDTOs = studentService.getAllCardsByFilter(pageable, filterStudentReq);

        return ResponseEntity.ok(studentCardDTOs);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<StudentDTO>> getAllByFilters(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterStudentReq filterStudentReq
    ) {
        PageResponse<StudentDTO> studentDTOs = studentService.getAllByFilter(pageable, filterStudentReq);

        return ResponseEntity.ok(studentDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setPhoto(
            @PathVariable @NotNull UUID id,

            @RequestPart("avatarFile") MultipartFile multipartFile
    ) {
        studentService.setPhoto(id, multipartFile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<StudentDTO> create(@Valid @RequestBody AddStudentReq addStudentReq) {
        StudentDTO studentDTO = studentService.create(addStudentReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/extended")
    public ResponseEntity<StudentDTO> createExtended(@Valid @RequestBody CreateStudentExtendedReq createStudentExtendedReq) {
        StudentDTO studentDTO = studentService.createExtended(createStudentExtendedReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> updateById(
            @PathVariable @NotNull UUID id,

            @Valid @RequestBody UpdateStudentReq updateStudentReq
    ) {
        StudentDTO studentDTO = studentService.update(id, updateStudentReq);

        return ResponseEntity.ok(studentDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> patchById(
            @PathVariable @NotNull UUID id,

            @Valid @RequestBody PatchStudentReq patchStudentReq
    ) {
        StudentDTO studentDTO = studentService.patch(id, patchStudentReq);

        return ResponseEntity.ok(studentDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
       studentService.deleteById(id);
    }
}
