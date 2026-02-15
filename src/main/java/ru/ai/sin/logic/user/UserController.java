package ru.ai.sin.logic.user;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.logic.user.dto.AddUserReq;
import ru.ai.sin.logic.user.dto.FilterUserReq;
import ru.ai.sin.logic.user.dto.UserDTO;
import ru.ai.sin.models.PageResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<UserDTO>> getByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @Valid @RequestBody FilterUserReq filterUserReq) {
        return ResponseEntity.ok(userService.getByFilter(pageable, filterUserReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<UserDTO> create(@Valid @RequestBody AddUserReq addUserReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(addUserReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
    }
}
