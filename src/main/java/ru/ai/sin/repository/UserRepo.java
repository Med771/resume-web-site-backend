package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.UserEnt;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEnt, UUID> {


    Optional<UserEnt> findByUserInformationUsername(String username);
}
