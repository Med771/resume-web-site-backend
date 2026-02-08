package ru.ai.sin.logic.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEnt, UUID> {

    boolean existsByUserInformationUsername(String username);
    Optional<UserEnt> findByUserInformationUsername(String username);
}
