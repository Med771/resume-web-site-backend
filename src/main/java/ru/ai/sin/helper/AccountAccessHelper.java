package ru.ai.sin.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.ForbiddenException;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.AccountStatus;
import ru.ai.sin.models.enums.RoleEnum;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountAccessHelper {

    private final SecurityHelper securityHelper;
    private final UserRepo userRepo;

    public UserEnt requireCurrentUser() {
        String username = securityHelper.getCurrentUsername();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ForbiddenException("Пользователь не найден"));
    }

    public void requireApprovedAccount() {
        UserEnt user = requireCurrentUser();
        if (user.getRole() == RoleEnum.ADMIN) {
            return;
        }
        if (user.getAccountStatus() != AccountStatus.APPROVED) {
            throw new ForbiddenException("Аккаунт ожидает одобрения администратором");
        }
    }

    public boolean isApprovedOrAdmin(UserEnt user) {
        if (user == null) {
            return false;
        }
        return user.getRole() == RoleEnum.ADMIN || user.getAccountStatus() == AccountStatus.APPROVED;
    }

    public boolean treatsAsAnonymousForCatalog(UserEnt user) {
        if (user == null) {
            return true;
        }
        if (user.getRole() == RoleEnum.ADMIN) {
            return false;
        }
        return user.getAccountStatus() != AccountStatus.APPROVED;
    }

    public UUID requireCurrentUserId() {
        return requireCurrentUser().getId();
    }

    /** null if not authenticated */
    public UserEnt requireCurrentUserOptional() {
        try {
            String username = securityHelper.getCurrentUsernameOptional().orElse(null);
            if (username == null) {
                return null;
            }
            return userRepo.findByUsername(username).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public void requireStudentOwnsProfile(UUID studentId) {
        UserEnt user = requireCurrentUser();
        if (user.getRole() == RoleEnum.ADMIN) {
            return;
        }
        if (user.getRole() != RoleEnum.STUDENT || user.getStudent() == null
                || !user.getStudent().getId().equals(studentId)) {
            throw new ForbiddenException("Нет доступа к профилю студента");
        }
    }

    public void rejectIfPendingCannotApply() {
        requireApprovedAccount();
    }

    public static void validateRejection(String reasonCode, String comment) {
        if (reasonCode == null || reasonCode.isBlank()) {
            throw new BadRequestException("Укажите причину отказа");
        }
    }
}
