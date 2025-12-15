package ru.ai.sin.service.tools;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.ai.sin.entity.UserEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserDetailsTools implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) {
        UserEnt user = userRepo.findByUserInformationUsername(username)
                .orElseThrow(() -> new NotFoundException("Failed to find user with username " + username));

        return  User.withUsername(user.getUserInformation().getUsername())
                .password(user.getUserInformation().getPasswordHash())
                .roles(user.getRole().getRole())
                .build();
    }
}
