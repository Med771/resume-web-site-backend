package ru.ai.sin.entity.spec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.entity.UserEnt;
import ru.ai.sin.entity.model.RoleEnum;
import ru.ai.sin.repository.UserRepo;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataInitializer implements ApplicationRunner {

    @Value("${app.logins[0].username}")
    private String guestUsername;
    @Value("${app.logins[0].password}")
    private String guestPassword;

    @Value("${app.logins[1].username}")
    private String userUsername;
    @Value("${app.logins[1].password}")
    private String userPassword;

    @Value("${app.logins[2].username}")
    private String adminUsername;
    @Value("${app.logins[2].password}")
    private String adminPassword;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        createIfNotExists(guestUsername, guestPassword, RoleEnum.GUEST);
        createIfNotExists(userUsername, userPassword, RoleEnum.USER);
        createIfNotExists(adminUsername, adminPassword, RoleEnum.ADMIN);
    }

    private void createIfNotExists(
            String username,
            String rawPassword,
            RoleEnum role
    ) {

        if (userRepo.existsByUserInformationUsername(username)) return;

        UserEnt user = userRepo.save(new UserEnt(role, username, passwordEncoder.encode(rawPassword)));

        log.info("User {} created", user.getId());
    }
}
