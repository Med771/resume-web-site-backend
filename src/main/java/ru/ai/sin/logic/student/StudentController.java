package ru.ai.sin.logic.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.ai.sin.exception.models.NotFoundException;

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
@Tag(name = "Student", description = "Операции управления студентами")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Текущий студент (ЛК)", description = "Профиль, привязанный к пользователю с ролью STUDENT")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(path = "/me")
    public ResponseEntity<StudentDTO> getMe() {
        return ResponseEntity.ok(studentService.getLinkedForCurrentUser()
                .orElseThrow(() -> new NotFoundException("К аккаунту не привязана карточка студента")));
    }

    @Operation(summary = "Получить студента по ID", description = "Возвращает полную карточку студента по UUID")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable @NotNull UUID id) {
        StudentDTO studentDTO = studentService.getById(id);

        return ResponseEntity.ok(studentDTO);
    }

    @Operation(summary = "Фильтр карточек студентов", description = "Принимает DTO фильтра в request body и Pageable без параметра sort")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/cardsFilter")
    public ResponseEntity<PageResponse<StudentCardDTO>> getCardsAllByFilters(
            @PageableDefault Pageable pageable,

            @Valid @RequestBody FilterStudentReq filterStudentReq
    ) {
        PageResponse<StudentCardDTO> studentCardDTOs = studentService.getAllCardsByFilter(pageable, filterStudentReq);

        return ResponseEntity.ok(studentCardDTOs);
    }

    @Operation(summary = "Фильтр студентов", description = "Принимает DTO фильтра в request body и Pageable без параметра sort")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<StudentDTO>> getAllByFilters(
            @PageableDefault Pageable pageable,

            @Valid @RequestBody FilterStudentReq filterStudentReq
    ) {
        PageResponse<StudentDTO> studentDTOs = studentService.getAllByFilter(pageable, filterStudentReq);

        return ResponseEntity.ok(studentDTOs);
    }

    @Operation(
            summary = "Загрузить фото студента",
            description = "Устанавливает или обновляет аватар. Форматы: JPEG/JFIF, PNG, GIF, WebP, BMP, HEIC/HEIF, AVIF, TIFF. "
                    + "Допускается application/octet-stream при корректном содержимом или расширении файла.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setPhoto(
            @PathVariable @NotNull UUID id,

            @RequestPart("avatarFile") MultipartFile multipartFile
    ) {
        studentService.setPhoto(id, multipartFile);
    }

    @Operation(summary = "Создать студента", description = "Создает студента по базовому DTO")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<StudentDTO> create(@Valid @RequestBody AddStudentReq addStudentReq) {
        StudentDTO studentDTO = studentService.create(addStudentReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @Operation(summary = "Создать студента расширенно", description = "Создает студента и опционально связанные portfolio, experiences, institutions и skills")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/extended")
    public ResponseEntity<StudentDTO> createExtended(@Valid @RequestBody CreateStudentExtendedReq createStudentExtendedReq) {
        StudentDTO studentDTO = studentService.createExtended(createStudentExtendedReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @Operation(summary = "Обновить студента полностью", description = "Полное обновление карточки студента (PUT)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> updateById(
            @PathVariable @NotNull UUID id,

            @Valid @RequestBody UpdateStudentReq updateStudentReq
    ) {
        StudentDTO studentDTO = studentService.update(id, updateStudentReq);

        return ResponseEntity.ok(studentDTO);
    }

    @Operation(summary = "Обновить студента частично", description = "Частичное обновление карточки студента (PATCH)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<StudentDTO> patchById(
            @PathVariable @NotNull UUID id,

            @Valid @RequestBody PatchStudentReq patchStudentReq
    ) {
        StudentDTO studentDTO = studentService.patch(id, patchStudentReq);

        return ResponseEntity.ok(studentDTO);
    }

    @Operation(summary = "Удалить студента", description = "Удаляет студента по UUID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
       studentService.deleteById(id);
    }
}
