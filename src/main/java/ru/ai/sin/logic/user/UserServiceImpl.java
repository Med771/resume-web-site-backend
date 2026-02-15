package ru.ai.sin.logic.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.user.dto.AddUserReq;
import ru.ai.sin.logic.user.dto.FilterUserReq;
import ru.ai.sin.logic.user.dto.UserDTO;
import ru.ai.sin.models.PageResponse;
import ru.ai.sin.models.enums.RoleEnum;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> getByFilter(Pageable pageable, FilterUserReq filterUserReq) {
        Page<UserEnt> page = userRepo.findAll(
                UserSpecifications.byFilters(filterUserReq),
                pageable
        );

        return new PageResponse<>(
                page.getContent().stream().map(userMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    @Transactional
    public UserDTO create(AddUserReq addUserReq) {
        if (userRepo.existsByUsername(addUserReq.username())) {
            throw new BadRequestException("User already exists: " + addUserReq.username());
        }

        UserEnt userEnt = new UserEnt(
                RoleEnum.USER,
                addUserReq.name(),
                addUserReq.username(),
                passwordEncoder.encode(addUserReq.password())
        );

        try {
            userEnt = userRepo.save(userEnt);
            log.info("Created user: {} with role USER", userEnt.getId());
            return userMapper.toDTO(userEnt);
        } catch (DataIntegrityViolationException ex) {
            log.warn("User already exists: {}", addUserReq.username());
            throw new BadRequestException("User already exists: " + addUserReq.username());
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        UserEnt userEnt = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (userEnt.getRole() != RoleEnum.USER) {
            throw new BadRequestException("Can only delete users with role USER");
        }

        userRepo.delete(userEnt);
        log.info("Deleted user: {}", id);
    }
}
