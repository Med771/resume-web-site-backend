package ru.ai.sin.logic.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.logic.account.dto.AccountApprovalUserDTO;
import ru.ai.sin.logic.account.dto.AccountRejectReq;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.PageResponse;
import ru.ai.sin.models.enums.AccountStatus;
import ru.ai.sin.models.enums.RoleEnum;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountApprovalServiceImpl implements AccountApprovalService {

    private final UserRepo userRepo;
    private final SecurityHelper securityHelper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AccountApprovalUserDTO> listPending(RoleEnum role, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        Page<UserEnt> result = role == null
                ? userRepo.findByAccountStatus(AccountStatus.PENDING_APPROVAL, pr)
                : userRepo.findByAccountStatusAndRole(AccountStatus.PENDING_APPROVAL, role, pr);
        return new PageResponse<>(
                result.getContent().stream().map(this::toDto).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    @Transactional
    public void approve(UUID userId) {
        UserEnt user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
        if (user.getAccountStatus() != AccountStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Аккаунт уже обработан");
        }
        user.setAccountStatus(AccountStatus.APPROVED);
        userRepo.save(user);
        log.info("Account approved: userId={} by {}", userId, securityHelper.getCurrentUsername());
    }

    @Override
    @Transactional
    public void reject(UUID userId, AccountRejectReq body) {
        UserEnt user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
        if (user.getAccountStatus() != AccountStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Аккаунт уже обработан");
        }
        user.setAccountStatus(AccountStatus.REJECTED);
        userRepo.save(user);
        log.info("Account rejected: userId={} by {} comment={}", userId, securityHelper.getCurrentUsername(),
                body != null ? body.comment() : null);
    }

    private AccountApprovalUserDTO toDto(UserEnt u) {
        return new AccountApprovalUserDTO(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getRole(),
                u.getAccountStatus(),
                u.getStudent() != null ? u.getStudent().getId() : null,
                u.getRecruiter() != null ? u.getRecruiter().getId() : null,
                null
        );
    }
}
